package com.bookstore.dao;

import com.bookstore.model.CartItem;

import java.util.List;

public interface CartRepository {

    List<CartItem> findByUserId(long userId);

    void addItem(long userId, long bookId, int qty);

    void updateItem(long userId, long bookId, int qty);

    void removeItem(long userId, long bookId);
}
