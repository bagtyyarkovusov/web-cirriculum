package com.bookstore.service;

import com.bookstore.dao.CartRepository;
import com.bookstore.model.CartItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartServiceTest {

    @Test
    void addItemRejectsNonPositiveQuantity() {
        RecordingCartRepository repo = new RecordingCartRepository();
        CartService service = new CartService(repo);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.addItem(1L, 2L, 0));

        assertEquals("数量必须大于0", error.getMessage());
        assertEquals(0, repo.addCalls);
    }

    @Test
    void updateItemRejectsNonPositiveQuantity() {
        RecordingCartRepository repo = new RecordingCartRepository();
        CartService service = new CartService(repo);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> service.updateItem(1L, 2L, -1));

        assertEquals("数量必须大于0", error.getMessage());
        assertEquals(0, repo.updateCalls);
    }

    @Test
    void totalOfSumsItemLineTotals() {
        List<CartItem> items = List.of(
                item(new BigDecimal("12.50"), 2),
                item(new BigDecimal("3.00"), 3));

        assertEquals(new BigDecimal("34.00"), CartService.totalOf(items));
    }

    private static CartItem item(BigDecimal price, int qty) {
        CartItem item = new CartItem();
        item.setBookPrice(price);
        item.setQty(qty);
        return item;
    }

    private static final class RecordingCartRepository implements CartRepository {
        private int addCalls;
        private int updateCalls;

        @Override
        public List<CartItem> findByUserId(long userId) {
            return List.of();
        }

        @Override
        public void addItem(long userId, long bookId, int qty) {
            addCalls++;
        }

        @Override
        public void updateItem(long userId, long bookId, int qty) {
            updateCalls++;
        }

        @Override
        public void removeItem(long userId, long bookId) {
        }
    }
}
