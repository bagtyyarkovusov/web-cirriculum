package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.web.SessionKeys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

final class CommandSupport {

    private CommandSupport() {
    }

    static SessionUser currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Object value = session == null ? null : session.getAttribute(SessionKeys.CURRENT_USER);
        if (value instanceof SessionUser user) {
            return user;
        }
        throw new IllegalStateException("请先登录。");
    }

    static long requiredLong(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("参数错误。");
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数错误。", e);
        }
    }

    static int optionalInt(HttpServletRequest req, String name, int defaultValue) {
        String value = req.getParameter(name);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数错误。", e);
        }
    }

    static Long optionalLong(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("参数错误。", e);
        }
    }

    static String optionalString(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value.trim();
    }

    static void redirect(HttpServletRequest req, HttpServletResponse resp, String path)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

    static void redirectWithMessage(HttpServletRequest req, HttpServletResponse resp,
                                    String path, String name, String message)
            throws IOException {
        String separator = path.contains("?") ? "&" : "?";
        redirect(req, resp, path + separator + name + "=" + encode(message));
    }

    static String operationError(RuntimeException e) {
        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
            return e.getMessage() == null ? "操作失败。" : e.getMessage();
        }
        return "操作失败，请稍后重试。";
    }

    private static String encode(String message) {
        return URLEncoder.encode(message, StandardCharsets.UTF_8);
    }
}
