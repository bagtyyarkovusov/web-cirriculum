package com.bookstore.filter;

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
import java.util.Set;

/**
 * Authentication filter with a 30-minute inactivity timeout.
 */
public class AuthFilter implements Filter {

    private static final long TIMEOUT_MILLIS = 30L * 60L * 1000L;
    private static final Set<String> PUBLIC_PATHS = Set.of("/books", "/login", "/register");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getPathInfo();
        if (path == null || "/".equals(path) || PUBLIC_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(SessionKeys.CURRENT_USER) == null) {
            redirectToLogin(req, resp, false);
            return;
        }

        long now = System.currentTimeMillis();
        Object lastActivity = session.getAttribute(SessionKeys.LAST_ACTIVITY_AT);
        if (lastActivity instanceof Long lastActivityAt && now - lastActivityAt > TIMEOUT_MILLIS) {
            session.invalidate();
            redirectToLogin(req, resp, true);
            return;
        }

        session.setAttribute(SessionKeys.LAST_ACTIVITY_AT, now);
        chain.doFilter(request, response);
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp, boolean timeout)
            throws IOException {
        String suffix = timeout ? "?timeout=1" : "";
        resp.sendRedirect(req.getContextPath() + "/app/login" + suffix);
    }
}
