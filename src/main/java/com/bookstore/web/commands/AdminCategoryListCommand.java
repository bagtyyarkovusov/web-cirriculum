package com.bookstore.web.commands;

import com.bookstore.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminCategoryListCommand implements Command {

    private final CategoryService categoryService;

    public AdminCategoryListCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("categories", categoryService.listCategories());
        req.setAttribute("message", req.getParameter("message"));
        req.setAttribute("error", req.getParameter("error"));
        return "admin/category/list";
    }
}
