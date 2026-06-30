package com.bookstore.service;

import com.bookstore.dao.UserRepository;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.security.Sm3Util;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private final LocalDateTime now = LocalDateTime.of(2026, 6, 30, 10, 0);

    @Test
    void loginSuccessResetsFailuresAndReturnsSessionUser() {
        FakeUserRepository repo = new FakeUserRepository();
        User user = user("customer", "Bookstore@123");
        user.setFailCount(2);
        repo.save(user);

        AuthResult result = new UserService(repo).login("customer", "Bookstore@123", now);

        assertEquals(AuthResult.Status.SUCCESS, result.getStatus());
        assertEquals("customer", result.getUser().getUsername());
        assertEquals(0, repo.saved.get("customer").getFailCount());
    }

    @Test
    void wrongPasswordIncrementsFailureCount() {
        FakeUserRepository repo = new FakeUserRepository();
        repo.save(user("customer", "Bookstore@123"));

        AuthResult result = new UserService(repo).login("customer", "wrong", now);

        assertEquals(AuthResult.Status.BAD_CREDENTIALS, result.getStatus());
        assertEquals(1, repo.saved.get("customer").getFailCount());
    }

    @Test
    void fifthWrongPasswordLocksAccountForThirtyMinutes() {
        FakeUserRepository repo = new FakeUserRepository();
        User user = user("customer", "Bookstore@123");
        user.setFailCount(4);
        repo.save(user);

        AuthResult result = new UserService(repo).login("customer", "wrong", now);

        assertEquals(AuthResult.Status.LOCKED, result.getStatus());
        assertEquals(now.plusMinutes(30), repo.saved.get("customer").getLockUntil());
    }

    @Test
    void lockedAccountCannotLoginUntilLockExpires() {
        FakeUserRepository repo = new FakeUserRepository();
        User user = user("customer", "Bookstore@123");
        user.setLockUntil(now.plusMinutes(1));
        repo.save(user);

        AuthResult result = new UserService(repo).login("customer", "Bookstore@123", now);

        assertEquals(AuthResult.Status.LOCKED, result.getStatus());
    }

    @Test
    void registerRejectsWeakPassword() {
        FakeUserRepository repo = new FakeUserRepository();

        RegistrationResult result = new UserService(repo)
                .register("newuser", "weak", "新用户");

        assertEquals(RegistrationResult.Status.WEAK_PASSWORD, result.getStatus());
    }

    @Test
    void registerCreatesCustomerWithSaltedSm3Hash() {
        FakeUserRepository repo = new FakeUserRepository();

        RegistrationResult result = new UserService(repo)
                .register("newuser", "Bookstore@123", "新用户");

        User inserted = repo.saved.get("newuser");
        assertEquals(RegistrationResult.Status.SUCCESS, result.getStatus());
        assertNotNull(inserted);
        assertEquals(Role.CUSTOMER, inserted.getRole());
        assertNotEquals("Bookstore@123", inserted.getPasswordSm3());
        assertTrue(Sm3Util.matches("Bookstore@123", inserted.getSalt(), inserted.getPasswordSm3()));
    }

    @Test
    void systemAdminCanResetOperatorAdminPassword() {
        FakeUserRepository repo = new FakeUserRepository();
        User operator = user("operator", "Bookstore@123");
        operator.setRole(Role.OPERATOR_ADMIN);
        operator.setFailCount(3);
        operator.setLockUntil(now.plusMinutes(5));
        repo.save(operator);
        String oldHash = operator.getPasswordSm3();

        boolean reset = new UserService(repo)
                .resetPassword(Role.SYSTEM_ADMIN, operator.getId(), "TempPass@123", now);

        User updated = repo.saved.get("operator");
        assertTrue(reset);
        assertNotEquals(oldHash, updated.getPasswordSm3());
        assertTrue(Sm3Util.matches("TempPass@123", updated.getSalt(), updated.getPasswordSm3()));
        assertEquals(0, updated.getFailCount());
        assertEquals(null, updated.getLockUntil());
        assertEquals(now, updated.getPwdChangedAt());
    }

    @Test
    void operatorAdminCannotResetPrivilegedAdminPassword() {
        FakeUserRepository repo = new FakeUserRepository();
        User sysadmin = user("sysadmin", "Bookstore@123");
        sysadmin.setRole(Role.SYSTEM_ADMIN);
        repo.save(sysadmin);
        String oldHash = sysadmin.getPasswordSm3();

        boolean reset = new UserService(repo)
                .resetPassword(Role.OPERATOR_ADMIN, sysadmin.getId(), "TempPass@123", now);

        assertFalse(reset);
        assertEquals(oldHash, repo.saved.get("sysadmin").getPasswordSm3());
    }

    private User user(String username, String password) {
        User user = new User();
        user.setId(username.hashCode());
        user.setUsername(username);
        user.setSalt(Sm3Util.newSalt());
        user.setPasswordSm3(Sm3Util.hashPassword(password, user.getSalt()));
        user.setRole(Role.CUSTOMER);
        user.setStatus("ACTIVE");
        user.setPwdChangedAt(now.minusDays(1));
        return user;
    }

    private static final class FakeUserRepository implements UserRepository {
        private final Map<String, User> saved = new LinkedHashMap<>();

        void save(User user) {
            saved.put(user.getUsername(), user);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return Optional.ofNullable(saved.get(username));
        }

        @Override
        public boolean usernameExists(String username) {
            return saved.containsKey(username);
        }

        @Override
        public List<User> findAll() {
            return List.copyOf(saved.values());
        }

        @Override
        public Optional<User> findById(long userId) {
            return saved.values().stream()
                    .filter(user -> user.getId() == userId)
                    .findFirst();
        }

        @Override
        public void insert(User user) {
            save(user);
        }

        @Override
        public void updateLoginFailure(long userId, int failCount, LocalDateTime lockUntil) {
            User user = requiredById(userId);
            user.setFailCount(failCount);
            user.setLockUntil(lockUntil);
        }

        @Override
        public void resetLoginFailures(long userId) {
            User user = requiredById(userId);
            user.setFailCount(0);
            user.setLockUntil(null);
        }

        @Override
        public boolean updateStatus(long userId, String status) {
            return findById(userId)
                    .map(user -> {
                        user.setStatus(status);
                        return true;
                    })
                    .orElse(false);
        }

        @Override
        public boolean resetPassword(long userId, String salt, String passwordSm3, LocalDateTime pwdChangedAt) {
            return findById(userId)
                    .map(user -> {
                        user.setSalt(salt);
                        user.setPasswordSm3(passwordSm3);
                        user.setPwdChangedAt(pwdChangedAt);
                        user.setFailCount(0);
                        user.setLockUntil(null);
                        return true;
                    })
                    .orElse(false);
        }

        private User requiredById(long userId) {
            return saved.values().stream()
                    .filter(user -> user.getId() == userId)
                    .findFirst()
                    .orElseThrow();
        }
    }
}
