package com.bookstore.service;

import com.bookstore.dao.StatsDao;
import com.bookstore.dao.StatsRepository;
import com.bookstore.model.SalesStat;

import java.math.BigDecimal;
import java.util.List;
import java.util.StringJoiner;

public class StatsService {

    private final StatsRepository statsRepository;

    public StatsService() {
        this(new StatsDao());
    }

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public List<SalesStat> dailySalesStats() {
        return statsRepository.findDailySales();
    }

    public SalesChartData dailySalesChartData() {
        return chartDataFor(dailySalesStats());
    }

    public SalesChartData chartDataFor(List<SalesStat> stats) {
        StringJoiner labels = new StringJoiner(",", "[", "]");
        StringJoiner totalSales = new StringJoiner(",", "[", "]");
        StringJoiner orderCounts = new StringJoiner(",", "[", "]");

        for (SalesStat stat : stats) {
            labels.add(jsonString(stat.getDay() == null ? "" : stat.getDay().toString()));
            totalSales.add(amount(stat.getTotalSales()).toPlainString());
            orderCounts.add(Integer.toString(stat.getOrderCount()));
        }

        return new SalesChartData(labels.toString(), totalSales.toString(), orderCounts.toString());
    }

    private BigDecimal amount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String jsonString(String value) {
        StringBuilder escaped = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> escaped.append(c);
            }
        }
        return escaped.append("\"").toString();
    }
}
