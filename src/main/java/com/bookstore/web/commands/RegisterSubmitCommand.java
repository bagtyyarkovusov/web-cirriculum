package com.bookstore.web.commands;

import com.bookstore.service.RegistrationResult;
import com.bookstore.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RegisterSubmitCommand implements Command {

    private final UserService userService;

    public RegisterSubmitCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String realName = req.getParameter("realName");
        RegistrationResult result = userService.register(username, req.getParameter("password"), realName);
        if (result.getStatus() == RegistrationResult.Status.SUCCESS) {
            resp.sendRedirect(req.getContextPath() + "/app/login?registered=1");
            return null;
        }

        req.setAttribute("username", username);
        req.setAttribute("realName", realName);
        req.setAttribute("error", messageFor(result));
        return "auth/register";
    }

    private String messageFor(RegistrationResult result) {
        return switch (result.getStatus()) {
            case USERNAME_TAKEN -> "用户名已存在。";
            case WEAK_PASSWORD -> "密码至少8位，且必须包含大写字母、小写字母、数字和特殊字符。";
            default -> "请填写用户名和密码。";
        };
    }
}
