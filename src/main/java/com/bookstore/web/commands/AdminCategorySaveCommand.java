package com.bookstore.web.commands;

import com.bookstore.model.Category;
import com.bookstore.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminCategorySaveCommand implements Command {

    private final CategoryService categoryService;

    public AdminCategorySaveCommand(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Category category = new Category();
            Long id = CommandSupport.optionalLong(req, "id");
            category.setId(id == null ? 0 : id);
            category.setName(CommandSupport.optionalString(req, "name"));
            categoryService.save(category);
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/categories",
                    "message", "分类已保存。");
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/categories",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
