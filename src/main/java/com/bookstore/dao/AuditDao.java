package com.bookstore.dao;

import com.bookstore.model.AuditLog;
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

/**
 * Plain-JDBC DAO for security audit rows.
 */
public class AuditDao implements AuditRepository {

    private static final String COLUMNS =
            "id, user_id, username, action, detail, ip, created_at, hmac";

    @Override
    public void insert(AuditLog log) {
        String sql = """
                INSERT INTO audit_log (user_id, username, action, detail, ip, created_at, hmac)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (log.getUserId() == null) {
                ps.setNull(1, Types.BIGINT);
            } else {
                ps.setLong(1, log.getUserId());
            }
            ps.setString(2, log.getUsername());
            ps.setString(3, log.getAction());
            ps.setString(4, log.getDetail());
            ps.setString(5, log.getIp());
            ps.setTimestamp(6, Timestamp.valueOf(log.getCreatedAt()));
            ps.setString(7, log.getHmac());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert audit log", e);
        }
    }

    @Override
    public List<AuditLog> search(String username, String action, String keyword,
                                 LocalDateTime from, LocalDateTime to, int limit) {
        StringBuilder sql = new StringBuilder("SELECT " + COLUMNS + " FROM audit_log WHERE 1 = 1");
        List<Binder> binders = new ArrayList<>();
        if (username != null) {
            sql.append(" AND username LIKE ?");
            binders.add((ps, index) -> ps.setString(index.next(), "%" + username + "%"));
        }
        if (action != null) {
            sql.append(" AND action = ?");
            binders.add((ps, index) -> ps.setString(index.next(), action));
        }
        if (keyword != null) {
            sql.append(" AND (detail LIKE ? OR action LIKE ? OR username LIKE ?)");
            binders.add((ps, index) -> {
                String value = "%" + keyword + "%";
                ps.setString(index.next(), value);
                ps.setString(index.next(), value);
                ps.setString(index.next(), value);
            });
        }
        if (from != null) {
            sql.append(" AND created_at >= ?");
            binders.add((ps, index) -> ps.setTimestamp(index.next(), Timestamp.valueOf(from)));
        }
        if (to != null) {
            sql.append(" AND created_at < ?");
            binders.add((ps, index) -> ps.setTimestamp(index.next(), Timestamp.valueOf(to)));
        }
        sql.append(" ORDER BY created_at DESC, id DESC LIMIT ?");

        List<AuditLog> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            BindingIndex index = new BindingIndex();
            for (Binder binder : binders) {
                binder.bind(ps, index);
            }
            ps.setInt(index.next(), Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search audit logs", e);
        }
        return result;
    }

    private AuditLog map(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getLong("id"));
        long userId = rs.getLong("user_id");
        log.setUserId(rs.wasNull() ? null : userId);
        log.setUsername(rs.getString("username"));
        log.setAction(rs.getString("action"));
        log.setDetail(rs.getString("detail"));
        log.setIp(rs.getString("ip"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        log.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        log.setHmac(rs.getString("hmac"));
        return log;
    }

    @FunctionalInterface
    private interface Binder {
        void bind(PreparedStatement ps, BindingIndex index) throws SQLException;
    }

    private static final class BindingIndex {
        private int value = 1;

        private int next() {
            return value++;
        }
    }
}
