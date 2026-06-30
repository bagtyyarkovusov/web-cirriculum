package com.bookstore.web.commands;

import com.bookstore.model.AuditLog;
import com.bookstore.service.AuditService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AdminAuditListCommand implements Command {

    private final AuditService auditService;

    public AdminAuditListCommand(AuditService auditService) {
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String username = CommandSupport.optionalString(req, "username");
        String action = CommandSupport.optionalString(req, "action");
        String keyword = CommandSupport.optionalString(req, "keyword");
        String from = CommandSupport.optionalString(req, "from");
        String to = CommandSupport.optionalString(req, "to");

        try {
            List<AuditLog> logs = auditService.search(
                    username, action, keyword, parseStart(from), parseEnd(to));
            req.setAttribute("logs", logs);
        } catch (DateTimeParseException e) {
            req.setAttribute("logs", List.of());
            req.setAttribute("error", "日期格式错误。");
        }

        req.setAttribute("username", username);
        req.setAttribute("action", action);
        req.setAttribute("keyword", keyword);
        req.setAttribute("from", from);
        req.setAttribute("to", to);
        return "admin/audit/list";
    }

    private LocalDateTime parseStart(String value) {
        return value == null || value.isBlank() ? null : LocalDate.parse(value).atStartOfDay();
    }

    private LocalDateTime parseEnd(String value) {
        return value == null || value.isBlank() ? null : LocalDate.parse(value).plusDays(1).atStartOfDay();
    }
}
