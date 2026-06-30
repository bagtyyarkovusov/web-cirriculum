package com.bookstore.web.commands;

import com.bookstore.service.AuthResult;
import com.bookstore.service.UserService;
import com.bookstore.web.SessionKeys;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginSubmitCommand implements Command {

    private final UserService userService;

    public LoginSubmitCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        AuthResult result = userService.login(username, req.getParameter("password"), LocalDateTime.now());
        if (result.getStatus() == AuthResult.Status.SUCCESS) {
            HttpSession session = req.getSession(true);
            session.setAttribute(SessionKeys.CURRENT_USER, result.getUser());
            session.setAttribute(SessionKeys.LAST_ACTIVITY_AT, System.currentTimeMillis());
            resp.sendRedirect(req.getContextPath() + "/app/books");
            return null;
        }

        req.setAttribute("username", username);
        req.setAttribute("error", messageFor(result));
        return "auth/login";
    }

    private String messageFor(AuthResult result) {
        return switch (result.getStatus()) {
            case LOCKED -> "连续登录失败过多，账号已锁定30分钟。";
            case DISABLED -> "账号已被禁用，请联系管理员。";
            default -> "用户名或密码错误。";
        };
    }
}
