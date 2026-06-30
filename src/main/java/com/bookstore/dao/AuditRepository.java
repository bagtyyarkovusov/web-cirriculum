package com.bookstore.dao;

import com.bookstore.model.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditRepository {

    void insert(AuditLog log);

    List<AuditLog> search(String username, String action, String keyword,
                          LocalDateTime from, LocalDateTime to, int limit);
}
