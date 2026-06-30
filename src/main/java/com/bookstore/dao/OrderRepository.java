package com.bookstore.dao;

import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    long checkoutPaidOrder(long userId, String orderNo, LocalDateTime now);

    List<Order> findByUserId(long userId);

    List<Order> findAllAdmin();

    Optional<Order> findByIdForUser(long userId, long orderId);

    Optional<Order> findByIdAdmin(long orderId);

    List<OrderItem> findItems(long orderId);

    boolean cancelIfCancellable(long userId, long orderId, LocalDateTime now);

    boolean confirmIfShipped(long userId, long orderId, LocalDateTime now);

    boolean shipIfPending(long orderId, String trackingNo, LocalDateTime now);
}
