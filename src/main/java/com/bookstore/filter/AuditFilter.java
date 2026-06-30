package com.bookstore.filter;

import com.bookstore.model.SessionUser;
import com.bookstore.service.AuditService;
import com.bookstore.web.SessionKeys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Route-level audit logging for sensitive read-only admin views.
 */
public class AuditFilter implements Filter {

    private final AuditService auditService;

    public AuditFilter() {
        this(new AuditService());
    }

    public AuditFilter(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        HttpServletRequest req = (HttpServletRequest) request;
        if (!"GET".equalsIgnoreCase(req.getMethod()) || !isSensitiveView(req.getPathInfo())) {
            return;
        }

        String path = req.getPathInfo();
        String action = isAuditPath(path) ? "AUDIT_VIEW" : "ADMIN_VIEW";
        try {
            auditService.log(currentUser(req.getSession(false)), action,
                    "method=" + req.getMethod() + ",path=" + path,
                    req.getRemoteAddr());
        } catch (RuntimeException ignored) {
            // Audit writes should not break an already-rendered read-only page.
        }
    }

    private boolean isSensitiveView(String path) {
        return isAuditPath(path)
                || (path != null && (path.startsWith("/admin/users")
                || path.startsWith("/admin/orders")));
    }

    private boolean isAuditPath(String path) {
        return path != null && path.startsWith("/admin/audit");
    }

    private SessionUser currentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(SessionKeys.CURRENT_USER);
        return user instanceof SessionUser sessionUser ? sessionUser : null;
    }
}
