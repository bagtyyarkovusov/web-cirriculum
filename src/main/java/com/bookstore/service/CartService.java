package com.bookstore.service;

import com.bookstore.dao.CartDao;
import com.bookstore.dao.CartRepository;
import com.bookstore.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartService {

    private static final String POSITIVE_QTY_MESSAGE = "数量必须大于0";

    private final CartRepository cartRepository;

    public CartService() {
        this(new CartDao());
    }

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<CartItem> listItems(long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void addItem(long userId, long bookId, int qty) {
        requirePositive(qty);
        cartRepository.addItem(userId, bookId, qty);
    }

    public void updateItem(long userId, long bookId, int qty) {
        requirePositive(qty);
        cartRepository.updateItem(userId, bookId, qty);
    }

    public void removeItem(long userId, long bookId) {
        cartRepository.removeItem(userId, bookId);
    }

    public static BigDecimal totalOf(List<CartItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getLineTotal());
        }
        return total.setScale(2);
    }

    private void requirePositive(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException(POSITIVE_QTY_MESSAGE);
        }
    }
}
