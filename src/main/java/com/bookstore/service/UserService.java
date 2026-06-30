package com.bookstore.service;

import com.bookstore.dao.UserDao;
import com.bookstore.dao.UserRepository;
import com.bookstore.model.Role;
import com.bookstore.model.SessionUser;
import com.bookstore.model.User;
import com.bookstore.security.LoginGuard;
import com.bookstore.security.PasswordPolicy;
import com.bookstore.security.Sm3Util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Authentication service: SM3 password verification, password policy, and lockout.
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this(new UserDao());
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResult login(String username, String password, LocalDateTime now) {
        String normalizedUsername = normalize(username);
        if (normalizedUsername.isEmpty() || password == null) {
            return AuthResult.badCredentials();
        }

        return userRepository.findByUsername(normalizedUsername)
                .map(user -> loginExistingUser(user, password, now))
                .orElseGet(AuthResult::badCredentials);
    }

    public RegistrationResult register(String username, String password, String realName) {
        String normalizedUsername = normalize(username);
        if (normalizedUsername.isEmpty() || password == null) {
            return RegistrationResult.invalidInput();
        }
        if (!PasswordPolicy.isValid(password)) {
            return RegistrationResult.weakPassword();
        }
        if (userRepository.usernameExists(normalizedUsername)) {
            return RegistrationResult.usernameTaken();
        }

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setSalt(Sm3Util.newSalt());
        user.setPasswordSm3(Sm3Util.hashPassword(password, user.getSalt()));
        user.setRealName(emptyToNull(realName));
        user.setRole(Role.CUSTOMER);
        user.setStatus("ACTIVE");
        user.setFailCount(0);
        user.setPwdChangedAt(LocalDateTime.now());
        userRepository.insert(user);
        return RegistrationResult.success();
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public boolean updateUserStatus(Role actorRole, long userId, String status) {
        String normalizedStatus = normalizeStatus(status);
        Optional<User> target = userRepository.findById(userId);
        if (target.isEmpty() || !canManage(actorRole, target.get().getRole())) {
            return false;
        }
        return userRepository.updateStatus(userId, normalizedStatus);
    }

    public boolean resetPassword(Role actorRole, long userId, String temporaryPassword) {
        return resetPassword(actorRole, userId, temporaryPassword, LocalDateTime.now());
    }

    boolean resetPassword(Role actorRole, long userId, String temporaryPassword, LocalDateTime now) {
        if (!PasswordPolicy.isValid(temporaryPassword)) {
            throw new IllegalArgumentException("临时密码不符合复杂度要求。");
        }
        Optional<User> target = userRepository.findById(userId);
        if (target.isEmpty() || !canManage(actorRole, target.get().getRole())) {
            return false;
        }
        String salt = Sm3Util.newSalt();
        String passwordSm3 = Sm3Util.hashPassword(temporaryPassword, salt);
        return userRepository.resetPassword(userId, salt, passwordSm3, now);
    }

    private AuthResult loginExistingUser(User user, String password, LocalDateTime now) {
        if (!"ACTIVE".equals(user.getStatus())) {
            return AuthResult.disabled();
        }
        if (LoginGuard.isLocked(user.getLockUntil(), now)) {
            return AuthResult.locked(user.getLockUntil());
        }
        if (!Sm3Util.matches(password, user.getSalt(), user.getPasswordSm3())) {
            LoginGuard.FailureState state = LoginGuard.recordFailure(user.getFailCount(), now);
            userRepository.updateLoginFailure(user.getId(), state.getFailCount(), state.getLockUntil());
            return state.getLockUntil() == null
                    ? AuthResult.badCredentials()
                    : AuthResult.locked(state.getLockUntil());
        }

        userRepository.resetLoginFailures(user.getId());
        return AuthResult.success(SessionUser.from(user));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String emptyToNull(String value) {
        String normalized = normalize(value);
        return normalized.isEmpty() ? null : normalized;
    }

    private String normalizeStatus(String status) {
        String normalized = normalize(status).toUpperCase();
        if (!"ACTIVE".equals(normalized) && !"DISABLED".equals(normalized)) {
            throw new IllegalArgumentException("账号状态参数错误。");
        }
        return normalized;
    }

    private boolean canManage(Role actorRole, Role targetRole) {
        if (actorRole == Role.SYSTEM_ADMIN) {
            return targetRole != Role.SYSTEM_ADMIN;
        }
        if (actorRole == Role.OPERATOR_ADMIN) {
            return targetRole == Role.CUSTOMER;
        }
        return false;
    }
}
