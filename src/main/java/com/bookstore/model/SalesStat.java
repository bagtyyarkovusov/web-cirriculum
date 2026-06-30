package com.bookstore.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesStat implements Serializable {

    private LocalDate day;
    private int orderCount;
    private BigDecimal totalSales;

    public SalesStat() {
    }

    public SalesStat(LocalDate day, int orderCount, BigDecimal totalSales) {
        this.day = day;
        this.orderCount = orderCount;
        this.totalSales = totalSales;
    }

    public LocalDate getDay() { return day; }
    public void setDay(LocalDate day) { this.day = day; }

    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }

    public BigDecimal getTotalSales() { return totalSales; }
    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
}
