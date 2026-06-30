package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.AuditService;
import com.bookstore.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminUserStatusCommand implements Command {

    private final UserService userService;
    private final AuditService auditService;

    public AdminUserStatusCommand(UserService userService, AuditService auditService) {
        this.userService = userService;
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            SessionUser actor = CommandSupport.currentUser(req);
            long userId = CommandSupport.requiredLong(req, "id");
            String status = CommandSupport.optionalString(req, "status");
            boolean changed = userService.updateUserStatus(actor.getRole(), userId, status);
            if (changed) {
                CommandSupport.audit(req, auditService, "USER_STATUS",
                        "targetUserId=" + userId + ",status=" + status);
                CommandSupport.redirectWithMessage(req, resp, "/app/admin/users",
                        "message", "账号状态已更新。");
            } else {
                CommandSupport.redirectWithMessage(req, resp, "/app/admin/users",
                        "error", "无权管理该账号或账号不存在。");
            }
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/users",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
