package com.bookstore.web.commands;

import com.bookstore.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminCategoryDeleteCommand implements Command {

    private final CategoryService categoryService;

    public AdminCategoryDeleteCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long id = CommandSupport.requiredLong(req, "id");
            boolean deleted = categoryService.deleteIfUnused(id);
            if (deleted) {
                CommandSupport.redirectWithMessage(req, resp, "/app/admin/categories",
                        "message", "分类已删除。");
            } else {
                CommandSupport.redirectWithMessage(req, resp, "/app/admin/categories",
                        "error", "分类不存在或仍有关联图书，不能删除。");
            }
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/categories",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
