package com.bookstore.filter;

import com.bookstore.model.SessionUser;
import com.bookstore.security.RbacPolicy;
import com.bookstore.web.SessionKeys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Role gate for authenticated admin routes.
 */
public class RbacFilter implements Filter {

    private final RbacPolicy policy = new RbacPolicy();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        SessionUser user = currentUser(req.getSession(false));
        if (user == null || !policy.canAccessAdminPath(user.getRole(), req.getPathInfo())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }

    private SessionUser currentUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute(SessionKeys.CURRENT_USER);
        return user instanceof SessionUser sessionUser ? sessionUser : null;
    }
}
