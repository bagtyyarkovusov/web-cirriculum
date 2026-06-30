package com.bookstore.service;

import com.bookstore.dao.AuditDao;
import com.bookstore.dao.AuditRepository;
import com.bookstore.model.AuditLog;
import com.bookstore.model.SessionUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class AuditService {

    private static final int DEFAULT_LIMIT = 200;

    private final AuditRepository auditRepository;
    private final Supplier<LocalDateTime> clock;

    public AuditService() {
        this(new AuditDao(), LocalDateTime::now);
    }

    public AuditService(AuditRepository auditRepository, Supplier<LocalDateTime> clock) {
        this.auditRepository = auditRepository;
        this.clock = clock;
    }

    public void log(SessionUser user, String action, String detail, String ip) {
        String normalizedAction = requiredAction(action);
        AuditLog log = new AuditLog();
        if (user != null) {
            log.setUserId(user.getId());
            log.setUsername(user.getUsername());
        }
        log.setAction(normalizedAction);
        log.setDetail(limit(normalize(detail), 1000));
        log.setIp(limit(normalize(ip), 45));
        log.setCreatedAt(clock.get());
        auditRepository.insert(log);
    }

    public List<AuditLog> search(String username, String action, String keyword,
                                 LocalDateTime from, LocalDateTime to) {
        return auditRepository.search(
                blankToNull(username),
                normalizeActionFilter(action),
                blankToNull(keyword),
                from,
                to,
                DEFAULT_LIMIT);
    }

    private String requiredAction(String action) {
        String normalized = blankToNull(action);
        if (normalized == null) {
            throw new IllegalArgumentException("审计动作不能为空。");
        }
        return normalized.toUpperCase(Locale.ROOT);
    }

    private String normalizeActionFilter(String action) {
        String normalized = blankToNull(action);
        return normalized == null ? null : normalized.toUpperCase(Locale.ROOT);
    }

    private String blankToNull(String value) {
        String normalized = normalize(value);
        return normalized.isEmpty() ? null : normalized;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private String limit(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
