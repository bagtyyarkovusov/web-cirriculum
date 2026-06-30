package com.bookstore.web.commands;

import com.bookstore.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminUserListCommand implements Command {

    private final UserService userService;

    public AdminUserListCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("users", userService.listUsers());
        req.setAttribute("tempPassword", "TempPass@123");
        req.setAttribute("message", req.getParameter("message"));
        req.setAttribute("error", req.getParameter("error"));
        return "admin/user/list";
    }
}
