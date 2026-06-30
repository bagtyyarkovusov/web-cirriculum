package com.bookstore.security;

import com.bookstore.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RbacPolicyTest {

    private final RbacPolicy policy = new RbacPolicy();

    @Test
    void auditAdminMayAccessAuditAdminPathsOnly() {
        assertTrue(policy.canAccessAdminPath(Role.AUDIT_ADMIN, "/admin/audit"));
        assertTrue(policy.canAccessAdminPath(Role.AUDIT_ADMIN, "/admin/audit/events"));

        assertFalse(policy.canAccessAdminPath(Role.AUDIT_ADMIN, "/admin"));
        assertFalse(policy.canAccessAdminPath(Role.AUDIT_ADMIN, "/admin/books"));
    }

    @Test
    void operatorAndSystemAdminsMayAccessNonAuditAdminPathsOnly() {
        assertTrue(policy.canAccessAdminPath(Role.OPERATOR_ADMIN, "/admin"));
        assertTrue(policy.canAccessAdminPath(Role.OPERATOR_ADMIN, "/admin/books"));
        assertTrue(policy.canAccessAdminPath(Role.SYSTEM_ADMIN, "/admin/users"));

        assertFalse(policy.canAccessAdminPath(Role.OPERATOR_ADMIN, "/admin/audit"));
        assertFalse(policy.canAccessAdminPath(Role.SYSTEM_ADMIN, "/admin/audit/events"));
    }

    @Test
    void customerAndMissingRoleCannotAccessAdminPaths() {
        assertFalse(policy.canAccessAdminPath(Role.CUSTOMER, "/admin"));
        assertFalse(policy.canAccessAdminPath(Role.CUSTOMER, "/admin/audit"));
        assertFalse(policy.canAccessAdminPath(null, "/admin"));
    }
}
