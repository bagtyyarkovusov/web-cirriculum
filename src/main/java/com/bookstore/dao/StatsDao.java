package com.bookstore.dao;

import com.bookstore.model.SalesStat;
import com.bookstore.util.Db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsDao implements StatsRepository {

    @Override
    public List<SalesStat> findDailySales() {
        String sql = """
                SELECT DATE(COALESCE(o.paid_at, o.created_at)) AS stat_day,
                       COUNT(DISTINCT o.id) AS order_count,
                       COALESCE(SUM(oi.price_snapshot * oi.qty), 0) AS total_sales
                  FROM orders o
                  JOIN order_item oi ON oi.order_id = o.id
                 WHERE o.status IN ('PENDING_SHIP', 'SHIPPED', 'COMPLETED')
                 GROUP BY DATE(COALESCE(o.paid_at, o.created_at))
                 ORDER BY stat_day
                """;
        List<SalesStat> result = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load sales statistics", e);
        }
        return result;
    }

    private SalesStat map(ResultSet rs) throws SQLException {
        SalesStat stat = new SalesStat();
        Date day = rs.getDate("stat_day");
        stat.setDay(day == null ? null : day.toLocalDate());
        stat.setOrderCount(rs.getInt("order_count"));
        stat.setTotalSales(rs.getBigDecimal("total_sales"));
        return stat;
    }
}
