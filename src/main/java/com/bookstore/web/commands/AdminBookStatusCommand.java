package com.bookstore.web.commands;

import com.bookstore.service.AuditService;
import com.bookstore.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminBookStatusCommand implements Command {

    private final BookService bookService;
    private final AuditService auditService;

    public AdminBookStatusCommand(BookService bookService, AuditService auditService) {
        this.bookService = bookService;
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long id = CommandSupport.requiredLong(req, "id");
            String status = CommandSupport.optionalString(req, "status");
            boolean changed = bookService.updateStatus(id, status);
            if (changed) {
                CommandSupport.audit(req, auditService, "BOOK_STATUS",
                        "bookId=" + id + ",status=" + status);
            }
            String message = changed ? "图书状态已更新。" : "图书不存在。";
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/books", "message", message);
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/books",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
