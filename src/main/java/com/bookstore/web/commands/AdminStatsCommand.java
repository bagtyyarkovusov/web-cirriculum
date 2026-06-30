package com.bookstore.web.commands;

import com.bookstore.model.SalesStat;
import com.bookstore.service.SalesChartData;
import com.bookstore.service.StatsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class AdminStatsCommand implements Command {

    private final StatsService statsService;

    public AdminStatsCommand(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<SalesStat> stats = statsService.dailySalesStats();
        SalesChartData chartData = statsService.chartDataFor(stats);
        req.setAttribute("stats", stats);
        req.setAttribute("chartLabelsJson", chartData.getLabelsJson());
        req.setAttribute("chartTotalSalesJson", chartData.getTotalSalesJson());
        req.setAttribute("chartOrderCountsJson", chartData.getOrderCountsJson());
        return "admin/stats";
    }
}
