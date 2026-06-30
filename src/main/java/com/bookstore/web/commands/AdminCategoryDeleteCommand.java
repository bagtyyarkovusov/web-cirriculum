package com.bookstore.web.commands;

import com.bookstore.service.AuditService;
import com.bookstore.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminCategoryDeleteCommand implements Command {

    private final CategoryService categoryService;
    private final AuditService auditService;

    public AdminCategoryDeleteCommand(CategoryService categoryService, AuditService auditService) {
        this.categoryService = categoryService;
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long id = CommandSupport.requiredLong(req, "id");
            boolean deleted = categoryService.deleteIfUnused(id);
            if (deleted) {
                CommandSupport.audit(req, auditService, "CATEGORY_DELETE", "categoryId=" + id);
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
