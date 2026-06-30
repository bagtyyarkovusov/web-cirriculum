package com.bookstore.web.commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFormCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        if ("1".equals(req.getParameter("registered"))) {
            req.setAttribute("message", "注册成功，请登录。");
        }
        if ("1".equals(req.getParameter("timeout"))) {
            req.setAttribute("error", "登录已超时，请重新登录。");
        }
        return "auth/login";
    }
}
