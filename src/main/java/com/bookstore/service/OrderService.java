package com.bookstore.service;

import com.bookstore.dao.OrderDao;
import com.bookstore.dao.OrderRepository;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class OrderService {

    private static final DateTimeFormatter ORDER_NO_TIME =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final OrderRepository orderRepository;
    private final Supplier<LocalDateTime> clock;

    public OrderService() {
        this(new OrderDao(), LocalDateTime::now);
    }

    public OrderService(OrderRepository orderRepository, Supplier<LocalDateTime> clock) {
        this.orderRepository = orderRepository;
        this.clock = clock;
    }

    public long checkout(long userId) {
        LocalDateTime now = clock.get();
        return orderRepository.checkoutPaidOrder(userId, orderNo(userId, now), now);
    }

    public List<Order> listOrders(long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> listAdminOrders() {
        return orderRepository.findAllAdmin();
    }

    public Optional<Order> findOrder(long userId, long orderId) {
        return orderRepository.findByIdForUser(userId, orderId);
    }

    public Optional<Order> findAdminOrder(long orderId) {
        return orderRepository.findByIdAdmin(orderId);
    }

    public List<OrderItem> findItems(long orderId) {
        return orderRepository.findItems(orderId);
    }

    public boolean cancel(long userId, long orderId) {
        return orderRepository.cancelIfCancellable(userId, orderId, clock.get());
    }

    public boolean confirmReceipt(long userId, long orderId) {
        return orderRepository.confirmIfShipped(userId, orderId, clock.get());
    }

    public boolean shipOrder(long orderId, String trackingNo) {
        String normalizedTrackingNo = normalizeTrackingNo(trackingNo);
        if (normalizedTrackingNo.isEmpty()) {
            throw new IllegalArgumentException("快递单号不能为空。");
        }
        return orderRepository.shipIfPending(orderId, normalizedTrackingNo, clock.get());
    }

    private String orderNo(long userId, LocalDateTime now) {
        int userPart = Math.floorMod(userId, 10_000);
        int randomPart = ThreadLocalRandom.current().nextInt(10_000);
        return String.format(Locale.ROOT, "O%s%04d%04d",
                ORDER_NO_TIME.format(now), userPart, randomPart);
    }

    private String normalizeTrackingNo(String trackingNo) {
        return trackingNo == null ? "" : trackingNo.trim();
    }
}
