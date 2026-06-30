package com.bookstore.web.commands;

import com.bookstore.service.AuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LogoutCommand implements Command {

    private final AuditService auditService;

    public LogoutCommand(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            CommandSupport.audit(req, auditService, "LOGOUT", "path=/app/logout");
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/app/books");
        return null;
    }
}
