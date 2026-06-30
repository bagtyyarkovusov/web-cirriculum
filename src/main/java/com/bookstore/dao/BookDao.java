package com.bookstore.dao;

import com.bookstore.model.Book;
import com.bookstore.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Plain-JDBC DAO for {@link Book}.
 */
public class BookDao {

    private static final String COLUMNS =
            "id, title, author, publisher, isbn, price, stock, category_id, cover_path, intro, status, created_at";

    /** Books currently on sale (status = 'ON'), ordered by id. */
    public List<Book> findOnSale() {
        String sql = "SELECT " + COLUMNS + " FROM book WHERE status = 'ON' ORDER BY id";
        List<Book> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load books", e);
        }
        return result;
    }

    public List<Book> findAll() {
        String sql = "SELECT " + COLUMNS + " FROM book ORDER BY id";
        List<Book> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load admin books", e);
        }
        return result;
    }

    public Optional<Book> findById(long id) {
        String sql = "SELECT " + COLUMNS + " FROM book WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load book", e);
        }
    }

    public long insert(Book book) {
        String sql = """
                INSERT INTO book
                  (title, author, publisher, isbn, price, stock, category_id, cover_path, intro, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindBook(ps, book);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
            throw new SQLException("No generated book id");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert book", e);
        }
    }

    public boolean update(Book book) {
        String sql = """
                UPDATE book
                   SET title = ?, author = ?, publisher = ?, isbn = ?, price = ?,
                       stock = ?, category_id = ?, cover_path = ?, intro = ?, status = ?
                 WHERE id = ?
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bindBook(ps, book);
            ps.setLong(11, book.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update book", e);
        }
    }

    public boolean updateStatus(long id, String status) {
        String sql = "UPDATE book SET status = ? WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update book status", e);
        }
    }

    private void bindBook(PreparedStatement ps, Book book) throws SQLException {
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getPublisher());
        ps.setString(4, book.getIsbn());
        ps.setBigDecimal(5, book.getPrice());
        ps.setInt(6, book.getStock());
        setLongOrNull(ps, 7, book.getCategoryId());
        ps.setString(8, book.getCoverPath());
        ps.setString(9, book.getIntro());
        ps.setString(10, book.getStatus());
    }

    private Book map(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getLong("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setPublisher(rs.getString("publisher"));
        b.setIsbn(rs.getString("isbn"));
        b.setPrice(rs.getBigDecimal("price"));
        b.setStock(rs.getInt("stock"));
        long categoryId = rs.getLong("category_id");
        b.setCategoryId(rs.wasNull() ? null : categoryId);
        b.setCoverPath(rs.getString("cover_path"));
        b.setIntro(rs.getString("intro"));
        b.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        b.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
        return b;
    }

    private void setLongOrNull(PreparedStatement ps, int index, Long value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setLong(index, value);
        }
    }
}
