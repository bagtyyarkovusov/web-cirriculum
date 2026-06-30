package com.bookstore.security;

import com.bookstore.model.Role;

/**
 * Central role decision for admin routes.
 */
public class RbacPolicy {

    public boolean canAccessAdminPath(Role role, String path) {
        if (role == null || !isAdminPath(path)) {
            return false;
        }
        if (isAuditPath(path)) {
            return role == Role.AUDIT_ADMIN;
        }
        return role == Role.OPERATOR_ADMIN || role == Role.SYSTEM_ADMIN;
    }

    private boolean isAdminPath(String path) {
        return "/admin".equals(path) || (path != null && path.startsWith("/admin/"));
    }

    private boolean isAuditPath(String path) {
        return path != null && path.startsWith("/admin/audit");
    }
}
