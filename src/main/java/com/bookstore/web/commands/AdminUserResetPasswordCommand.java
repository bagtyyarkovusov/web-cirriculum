package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.AuditService;
import com.bookstore.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminUserResetPasswordCommand implements Command {

    private final UserService userService;
    private final AuditService auditService;

    public AdminUserResetPasswordCommand(UserService userService, AuditService auditService) {
        this.userService = userService;
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            SessionUser actor = CommandSupport.currentUser(req);
            long userId = CommandSupport.requiredLong(req, "id");
            String temporaryPassword = CommandSupport.optionalString(req, "temporaryPassword");
            boolean reset = userService.resetPassword(actor.getRole(), userId, temporaryPassword);
            if (reset) {
                CommandSupport.audit(req, auditService, "USER_RESET_PASSWORD",
                        "targetUserId=" + userId);
                CommandSupport.redirectWithMessage(req, resp, "/app/admin/users",
                        "message", "密码已重置为：" + temporaryPassword);
            } else {
                CommandSupport.redirectWithMessage(req, resp, "/app/admin/users",
                        "error", "无权重置该账号或账号不存在。");
            }
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/users",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
