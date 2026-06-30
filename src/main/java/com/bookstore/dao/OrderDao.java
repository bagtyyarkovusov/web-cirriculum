package com.bookstore.dao;

import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.model.OrderStatus;
import com.bookstore.util.Db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao implements OrderRepository {

    private static final String ORDER_COLUMNS =
            "id, order_no, user_id, total, status, receiver_snapshot, tracking_no, " +
                    "created_at, paid_at, shipped_at, completed_at";

    @Override
    public long checkoutPaidOrder(long userId, String orderNo, LocalDateTime now) {
        try (Connection conn = Db.getConnection()) {
            conn.setAutoCommit(false);
            try {
                List<CartItem> items = lockCartItems(conn, userId);
                if (items.isEmpty()) {
                    throw new IllegalStateException("购物车为空，无法结算。");
                }
                for (CartItem item : items) {
                    assertCheckoutable(item);
                }

                BigDecimal total = totalOf(items);
                long orderId = insertPaidOrder(conn, userId, orderNo, total, now);
                for (CartItem item : items) {
                    insertOrderItem(conn, orderId, item);
                    decreaseStock(conn, item);
                }
                clearCart(conn, userId);

                conn.commit();
                return orderId;
            } catch (SQLException e) {
                rollback(conn);
                throw new RuntimeException("Failed to checkout order", e);
            } catch (RuntimeException e) {
                rollback(conn);
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open checkout transaction", e);
        }
    }

    @Override
    public List<Order> findByUserId(long userId) {
        String sql = "SELECT " + ORDER_COLUMNS + " FROM orders WHERE user_id = ? ORDER BY created_at DESC, id DESC";
        List<Order> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapOrder(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load orders", e);
        }
        return result;
    }

    @Override
    public List<Order> findAllAdmin() {
        String sql = "SELECT " + ORDER_COLUMNS + " FROM orders ORDER BY created_at DESC, id DESC";
        List<Order> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load admin orders", e);
        }
        return result;
    }

    @Override
    public Optional<Order> findByIdForUser(long userId, long orderId) {
        String sql = "SELECT " + ORDER_COLUMNS + " FROM orders WHERE user_id = ? AND id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapOrder(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load order", e);
        }
    }

    @Override
    public Optional<Order> findByIdAdmin(long orderId) {
        String sql = "SELECT " + ORDER_COLUMNS + " FROM orders WHERE id = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapOrder(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load admin order", e);
        }
    }

    @Override
    public List<OrderItem> findItems(long orderId) {
        String sql = """
                SELECT id, order_id, book_id, title_snapshot, price_snapshot, qty
                  FROM order_item
                 WHERE order_id = ?
                 ORDER BY id
                """;
        List<OrderItem> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapOrderItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load order items", e);
        }
        return result;
    }

    @Override
    public boolean cancelIfCancellable(long userId, long orderId, LocalDateTime now) {
        try (Connection conn = Db.getConnection()) {
            conn.setAutoCommit(false);
            try {
                OrderStatus status = lockOrderStatus(conn, userId, orderId);
                if (status == null || !status.canCancel()) {
                    conn.rollback();
                    return false;
                }
                if (status == OrderStatus.PENDING_SHIP) {
                    restoreStock(conn, orderId);
                }
                String sql = "UPDATE orders SET status = 'CANCELLED' WHERE id = ? AND user_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setLong(1, orderId);
                    ps.setLong(2, userId);
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                rollback(conn);
                throw new RuntimeException("Failed to cancel order", e);
            } catch (RuntimeException e) {
                rollback(conn);
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open cancel transaction", e);
        }
    }

    @Override
    public boolean confirmIfShipped(long userId, long orderId, LocalDateTime now) {
        String sql = """
                UPDATE orders
                   SET status = 'COMPLETED', completed_at = ?
                 WHERE id = ? AND user_id = ? AND status = 'SHIPPED'
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setLong(2, orderId);
            ps.setLong(3, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to confirm order receipt", e);
        }
    }

    @Override
    public boolean shipIfPending(long orderId, String trackingNo, LocalDateTime now) {
        String sql = """
                UPDATE orders
                   SET status = 'SHIPPED', tracking_no = ?, shipped_at = ?
                 WHERE id = ? AND status = 'PENDING_SHIP'
                """;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trackingNo);
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setLong(3, orderId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to ship order", e);
        }
    }

    private List<CartItem> lockCartItems(Connection conn, long userId) throws SQLException {
        String sql = """
                SELECT ci.id, ci.user_id, ci.book_id, ci.qty,
                       b.title AS book_title, b.price AS book_price,
                       b.stock AS book_stock, b.status AS book_status
                  FROM cart_item ci
                  JOIN book b ON b.id = ci.book_id
                 WHERE ci.user_id = ?
                 ORDER BY ci.id
                 FOR UPDATE
                """;
        List<CartItem> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setBookId(rs.getLong("book_id"));
                    item.setQty(rs.getInt("qty"));
                    item.setBookTitle(rs.getString("book_title"));
                    item.setBookPrice(rs.getBigDecimal("book_price"));
                    item.setBookStock(rs.getInt("book_stock"));
                    item.setBookStatus(rs.getString("book_status"));
                    result.add(item);
                }
            }
        }
        return result;
    }

    private void assertCheckoutable(CartItem item) {
        if (item.getQty() <= 0) {
            throw new IllegalStateException("购物车中存在无效数量。");
        }
        if (!"ON".equals(item.getBookStatus())) {
            throw new IllegalStateException("图书已下架：" + item.getBookTitle());
        }
        if (item.getQty() > item.getBookStock()) {
            throw new IllegalStateException("库存不足：" + item.getBookTitle());
        }
    }

    private BigDecimal totalOf(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getLineTotal());
        }
        return total.setScale(2);
    }

    private long insertPaidOrder(Connection conn, long userId, String orderNo, BigDecimal total, LocalDateTime now)
            throws SQLException {
        // Mock payment: checkout creates a paid order directly in PENDING_SHIP.
        String sql = """
                INSERT INTO orders (order_no, user_id, total, status, created_at, paid_at)
                VALUES (?, ?, ?, 'PENDING_SHIP', ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, orderNo);
            ps.setLong(2, userId);
            ps.setBigDecimal(3, total);
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setTimestamp(5, Timestamp.valueOf(now));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        throw new SQLException("No generated order id");
    }

    private void insertOrderItem(Connection conn, long orderId, CartItem item) throws SQLException {
        String sql = """
                INSERT INTO order_item (order_id, book_id, title_snapshot, price_snapshot, qty)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, item.getBookId());
            ps.setString(3, item.getBookTitle());
            ps.setBigDecimal(4, item.getBookPrice());
            ps.setInt(5, item.getQty());
            ps.executeUpdate();
        }
    }

    private void decreaseStock(Connection conn, CartItem item) throws SQLException {
        String sql = "UPDATE book SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getQty());
            ps.setLong(2, item.getBookId());
            ps.setInt(3, item.getQty());
            if (ps.executeUpdate() != 1) {
                throw new IllegalStateException("库存不足：" + item.getBookTitle());
            }
        }
    }

    private void clearCart(Connection conn, long userId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cart_item WHERE user_id = ?")) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    private OrderStatus lockOrderStatus(Connection conn, long userId, long orderId) throws SQLException {
        String sql = "SELECT status FROM orders WHERE id = ? AND user_id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? OrderStatus.fromDb(rs.getString("status")) : null;
            }
        }
    }

    private void restoreStock(Connection conn, long orderId) throws SQLException {
        String sql = """
                UPDATE book b
                  JOIN order_item oi ON oi.book_id = b.id
                   SET b.stock = b.stock + oi.qty
                 WHERE oi.order_id = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.executeUpdate();
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderNo(rs.getString("order_no"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotal(rs.getBigDecimal("total"));
        order.setStatus(OrderStatus.fromDb(rs.getString("status")));
        order.setReceiverSnapshot(rs.getString("receiver_snapshot"));
        order.setTrackingNo(rs.getString("tracking_no"));
        order.setCreatedAt(toLocalDateTime(rs.getTimestamp("created_at")));
        order.setPaidAt(toLocalDateTime(rs.getTimestamp("paid_at")));
        order.setShippedAt(toLocalDateTime(rs.getTimestamp("shipped_at")));
        order.setCompletedAt(toLocalDateTime(rs.getTimestamp("completed_at")));
        return order;
    }

    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setBookId(rs.getLong("book_id"));
        item.setTitleSnapshot(rs.getString("title_snapshot"));
        item.setPriceSnapshot(rs.getBigDecimal("price_snapshot"));
        item.setQty(rs.getInt("qty"));
        return item;
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException ignored) {
        }
    }
}
