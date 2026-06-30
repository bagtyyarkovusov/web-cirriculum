package com.bookstore.service;

import com.bookstore.dao.CategoryDao;
import com.bookstore.model.Category;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    private final CategoryDao categoryDao = new CategoryDao();

    public List<Category> listCategories() {
        return categoryDao.findAll();
    }

    public Optional<Category> findCategory(long id) {
        return categoryDao.findById(id);
    }

    public long save(Category category) {
        category.setName(required(category.getName()));
        if (category.getId() > 0) {
            if (!categoryDao.update(category)) {
                throw new IllegalArgumentException("分类不存在。");
            }
            return category.getId();
        }
        return categoryDao.insert(category);
    }

    public boolean deleteIfUnused(long id) {
        return categoryDao.deleteIfUnused(id);
    }

    private String required(String value) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空。");
        }
        return normalized;
    }
}
