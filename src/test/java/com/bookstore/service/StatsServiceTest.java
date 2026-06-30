package com.bookstore.service;

import com.bookstore.dao.StatsRepository;
import com.bookstore.model.SalesStat;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsServiceTest {

    @Test
    void buildsChartJsonFromDailySalesStats() {
        StatsService service = new StatsService(new StaticStatsRepository(List.of(
                new SalesStat(LocalDate.of(2026, 6, 29), 2, new BigDecimal("123.40")),
                new SalesStat(LocalDate.of(2026, 6, 30), 1, new BigDecimal("49.00"))
        )));

        SalesChartData chartData = service.dailySalesChartData();

        assertEquals("[\"2026-06-29\",\"2026-06-30\"]", chartData.getLabelsJson());
        assertEquals("[123.40,49.00]", chartData.getTotalSalesJson());
        assertEquals("[2,1]", chartData.getOrderCountsJson());
    }

    @Test
    void emptyStatsProduceEmptyChartArrays() {
        StatsService service = new StatsService(new StaticStatsRepository(List.of()));

        SalesChartData chartData = service.dailySalesChartData();

        assertEquals("[]", chartData.getLabelsJson());
        assertEquals("[]", chartData.getTotalSalesJson());
        assertEquals("[]", chartData.getOrderCountsJson());
    }

    private static final class StaticStatsRepository implements StatsRepository {
        private final List<SalesStat> stats;

        private StaticStatsRepository(List<SalesStat> stats) {
            this.stats = stats;
        }

        @Override
        public List<SalesStat> findDailySales() {
            return stats;
        }
    }
}
