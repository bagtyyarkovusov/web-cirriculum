package com.bookstore.dao;

import com.bookstore.model.Category;
import com.bookstore.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDao {

    public List<Category> findAll() {
        String sql = "SELECT id, name, parent_id FROM category ORDER BY id";
        List<Category> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load categories", e);
        }
        return result;
    }

    public Optional<Category> findById(long id) {
        String sql = "SELECT id, name, parent_id FROM category WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load category", e);
        }
    }

    public long insert(Category category) {
        String sql = "INSERT INTO category (name, parent_id) VALUES (?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindCategory(ps, category);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
            throw new SQLException("No generated category id");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert category", e);
        }
    }

    public boolean update(Category category) {
        String sql = "UPDATE category SET name = ?, parent_id = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindCategory(ps, category);
            ps.setLong(3, category.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update category", e);
        }
    }

    public boolean deleteIfUnused(long id) {
        String sql = """
                DELETE FROM category
                 WHERE id = ?
                   AND NOT EXISTS (SELECT 1 FROM book WHERE category_id = ?)
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setLong(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete category", e);
        }
    }

    private void bindCategory(PreparedStatement ps, Category category) throws SQLException {
        ps.setString(1, category.getName());
        if (category.getParentId() == null) {
            ps.setNull(2, Types.BIGINT);
        } else {
            ps.setLong(2, category.getParentId());
        }
    }

    private Category map(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        long parentId = rs.getLong("parent_id");
        category.setParentId(rs.wasNull() ? null : parentId);
        return category;
    }
}
