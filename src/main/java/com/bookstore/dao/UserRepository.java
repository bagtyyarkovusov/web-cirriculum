package com.bookstore.dao;

import com.bookstore.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    boolean usernameExists(String username);

    List<User> findAll();

    Optional<User> findById(long userId);

    void insert(User user);

    void updateLoginFailure(long userId, int failCount, LocalDateTime lockUntil);

    void resetLoginFailures(long userId);

    boolean updateStatus(long userId, String status);

    boolean resetPassword(long userId, String salt, String passwordSm3, LocalDateTime pwdChangedAt);
}
