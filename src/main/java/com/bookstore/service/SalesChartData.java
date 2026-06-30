package com.bookstore.service;

public class SalesChartData {

    private final String labelsJson;
    private final String totalSalesJson;
    private final String orderCountsJson;

    public SalesChartData(String labelsJson, String totalSalesJson, String orderCountsJson) {
        this.labelsJson = labelsJson;
        this.totalSalesJson = totalSalesJson;
        this.orderCountsJson = orderCountsJson;
    }

    public String getLabelsJson() { return labelsJson; }

    public String getTotalSalesJson() { return totalSalesJson; }

    public String getOrderCountsJson() { return orderCountsJson; }
}
