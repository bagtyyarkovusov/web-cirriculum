package com.bookstore.service;

import com.bookstore.dao.OrderRepository;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceTest {

    @Test
    void checkoutPassesGeneratedOrderNoAndReturnsCreatedOrderId() {
        RecordingOrderRepository repo = new RecordingOrderRepository();
        LocalDateTime now = LocalDateTime.of(2026, 6, 30, 10, 5, 6);
        OrderService service = new OrderService(repo, () -> now);

        long orderId = service.checkout(7L);

        assertEquals(99L, orderId);
        assertEquals(7L, repo.checkoutUserId);
        assertEquals(now, repo.checkoutAt);
        assertTrue(repo.checkoutOrderNo.startsWith("O20260630100506"));
        assertTrue(repo.checkoutOrderNo.length() <= 32);
    }

    @Test
    void shipOrderTrimsTrackingNumberAndUsesCurrentTime() {
        RecordingOrderRepository repo = new RecordingOrderRepository();
        LocalDateTime now = LocalDateTime.of(2026, 6, 30, 11, 20);
        OrderService service = new OrderService(repo, () -> now);

        boolean shipped = service.shipOrder(42L, "  SF123456  ");

        assertTrue(shipped);
        assertEquals(42L, repo.shipOrderId);
        assertEquals("SF123456", repo.shipTrackingNo);
        assertEquals(now, repo.shippedAt);
    }

    @Test
    void shipOrderRejectsBlankTrackingNumber() {
        RecordingOrderRepository repo = new RecordingOrderRepository();
        OrderService service = new OrderService(repo, () -> LocalDateTime.of(2026, 6, 30, 11, 20));

        assertThrows(IllegalArgumentException.class, () -> service.shipOrder(42L, " "));
    }

    private static final class RecordingOrderRepository implements OrderRepository {
        private long checkoutUserId;
        private String checkoutOrderNo;
        private LocalDateTime checkoutAt;
        private long shipOrderId;
        private String shipTrackingNo;
        private LocalDateTime shippedAt;

        @Override
        public long checkoutPaidOrder(long userId, String orderNo, LocalDateTime now) {
            checkoutUserId = userId;
            checkoutOrderNo = orderNo;
            checkoutAt = now;
            return 99L;
        }

        @Override
        public List<Order> findByUserId(long userId) {
            return List.of();
        }

        @Override
        public List<Order> findAllAdmin() {
            return List.of();
        }

        @Override
        public Optional<Order> findByIdForUser(long userId, long orderId) {
            return Optional.empty();
        }

        @Override
        public Optional<Order> findByIdAdmin(long orderId) {
            return Optional.empty();
        }

        @Override
        public List<OrderItem> findItems(long orderId) {
            return List.of();
        }

        @Override
        public boolean cancelIfCancellable(long userId, long orderId, LocalDateTime now) {
            return false;
        }

        @Override
        public boolean confirmIfShipped(long userId, long orderId, LocalDateTime now) {
            return false;
        }

        @Override
        public boolean shipIfPending(long orderId, String trackingNo, LocalDateTime now) {
            shipOrderId = orderId;
            shipTrackingNo = trackingNo;
            shippedAt = now;
            return true;
        }
    }
}
