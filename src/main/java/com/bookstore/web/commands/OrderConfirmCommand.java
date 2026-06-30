package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderConfirmCommand implements Command {

    private final OrderService orderService;

    public OrderConfirmCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long orderId = CommandSupport.requiredLong(req, "id");
            SessionUser user = CommandSupport.currentUser(req);
            boolean confirmed = orderService.confirmReceipt(user.getId(), orderId);
            if (confirmed) {
                CommandSupport.redirectWithMessage(req, resp, "/app/orders/detail?id=" + orderId,
                        "message", "已确认收货。");
            } else {
                CommandSupport.redirectWithMessage(req, resp, "/app/orders/detail?id=" + orderId,
                        "error", "只有已发货订单可以确认收货。");
            }
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/orders",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
