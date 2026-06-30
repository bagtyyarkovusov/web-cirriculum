package com.bookstore.dao;

import com.bookstore.model.CartItem;
import com.bookstore.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDao implements CartRepository {

    @Override
    public List<CartItem> findByUserId(long userId) {
        String sql = """
                SELECT ci.id, ci.user_id, ci.book_id, ci.qty,
                       b.title AS book_title, b.price AS book_price,
                       b.stock AS book_stock, b.status AS book_status
                  FROM cart_item ci
                  JOIN book b ON b.id = ci.book_id
                 WHERE ci.user_id = ?
                 ORDER BY ci.id
                """;
        List<CartItem> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load cart", e);
        }
        return result;
    }

    @Override
    public void addItem(long userId, long bookId, int qty) {
        String sql = """
                INSERT INTO cart_item (user_id, book_id, qty)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE qty = qty + VALUES(qty)
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            ps.setInt(3, qty);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add cart item", e);
        }
    }

    @Override
    public void updateItem(long userId, long bookId, int qty) {
        String sql = "UPDATE cart_item SET qty = ? WHERE user_id = ? AND book_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setLong(2, userId);
            ps.setLong(3, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update cart item", e);
        }
    }

    @Override
    public void removeItem(long userId, long bookId) {
        String sql = "DELETE FROM cart_item WHERE user_id = ? AND book_id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }

    private CartItem map(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getLong("id"));
        item.setUserId(rs.getLong("user_id"));
        item.setBookId(rs.getLong("book_id"));
        item.setQty(rs.getInt("qty"));
        item.setBookTitle(rs.getString("book_title"));
        item.setBookPrice(rs.getBigDecimal("book_price"));
        item.setBookStock(rs.getInt("book_stock"));
        item.setBookStatus(rs.getString("book_status"));
        return item;
    }
}
