package com.bookstore.dao;

import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Plain-JDBC user DAO for authentication and registration.
 */
public class UserDao implements UserRepository {

    private static final String COLUMNS =
            "id, username, password_sm3, salt, real_name, gender, phone_enc, address_enc, " +
            "role, status, fail_count, lock_until, pwd_changed_at, created_at";

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT " + COLUMNS + " FROM `user` WHERE username = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load user", e);
        }
    }

    @Override
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM `user` WHERE username = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check username", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT " + COLUMNS + " FROM `user` ORDER BY id";
        List<User> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load users", e);
        }
        return result;
    }

    @Override
    public Optional<User> findById(long userId) {
        String sql = "SELECT " + COLUMNS + " FROM `user` WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load user", e);
        }
    }

    @Override
    public void insert(User user) {
        String sql = """
                INSERT INTO `user`
                  (username, password_sm3, salt, real_name, role, status, fail_count, pwd_changed_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordSm3());
            ps.setString(3, user.getSalt());
            ps.setString(4, user.getRealName());
            ps.setString(5, user.getRole().name());
            ps.setString(6, user.getStatus());
            ps.setInt(7, user.getFailCount());
            ps.setTimestamp(8, Timestamp.valueOf(user.getPwdChangedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }

    @Override
    public void updateLoginFailure(long userId, int failCount, LocalDateTime lockUntil) {
        String sql = "UPDATE `user` SET fail_count = ?, lock_until = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, failCount);
            setTimestampOrNull(ps, 2, lockUntil);
            ps.setLong(3, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update login failure", e);
        }
    }

    @Override
    public void resetLoginFailures(long userId) {
        String sql = "UPDATE `user` SET fail_count = 0, lock_until = NULL WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset login failures", e);
        }
    }

    @Override
    public boolean updateStatus(long userId, String status) {
        String sql = "UPDATE `user` SET status = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user status", e);
        }
    }

    @Override
    public boolean resetPassword(long userId, String salt, String passwordSm3, LocalDateTime pwdChangedAt) {
        String sql = """
                UPDATE `user`
                   SET salt = ?, password_sm3 = ?, fail_count = 0, lock_until = NULL, pwd_changed_at = ?
                 WHERE id = ?
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, salt);
            ps.setString(2, passwordSm3);
            ps.setTimestamp(3, Timestamp.valueOf(pwdChangedAt));
            ps.setLong(4, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset user password", e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordSm3(rs.getString("password_sm3"));
        user.setSalt(rs.getString("salt"));
        user.setRealName(rs.getString("real_name"));
        user.setGender(rs.getString("gender"));
        user.setPhoneEnc(rs.getString("phone_enc"));
        user.setAddressEnc(rs.getString("address_enc"));
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setStatus(rs.getString("status"));
        user.setFailCount(rs.getInt("fail_count"));
        user.setLockUntil(toLocalDateTime(rs.getTimestamp("lock_until")));
        user.setPwdChangedAt(toLocalDateTime(rs.getTimestamp("pwd_changed_at")));
        user.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
        return user;
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    private void setTimestampOrNull(PreparedStatement ps, int index, LocalDateTime value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(index, Timestamp.valueOf(value));
        }
    }
}
