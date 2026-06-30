package com.bookstore.dao;

import com.bookstore.model.SalesStat;

import java.util.List;

public interface StatsRepository {

    List<SalesStat> findDailySales();
}
