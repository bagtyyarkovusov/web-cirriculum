package com.bookstore.web.commands;

import com.bookstore.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminBookListCommand implements Command {

    private final BookService bookService;

    public AdminBookListCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("books", bookService.listAdminBooks());
        req.setAttribute("message", req.getParameter("message"));
        req.setAttribute("error", req.getParameter("error"));
        return "admin/book/list";
    }
}
