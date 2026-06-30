package com.bookstore.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Security audit event stored in audit_log.
 */
public class AuditLog implements Serializable {

    private long id;
    private Long userId;
    private String username;
    private String action;
    private String detail;
    private String ip;
    private LocalDateTime createdAt;
    private String hmac;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getHmac() { return hmac; }
    public void setHmac(String hmac) { this.hmac = hmac; }
}
