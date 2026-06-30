package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderCancelCommand implements Command {

    private final OrderService orderService;

    public OrderCancelCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long orderId = CommandSupport.requiredLong(req, "id");
            SessionUser user = CommandSupport.currentUser(req);
            boolean cancelled = orderService.cancel(user.getId(), orderId);
            if (cancelled) {
                CommandSupport.redirectWithMessage(req, resp, "/app/orders/detail?id=" + orderId,
                        "message", "订单已取消。");
            } else {
                CommandSupport.redirectWithMessage(req, resp, "/app/orders/detail?id=" + orderId,
                        "error", "当前订单状态不允许取消。");
            }
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/orders",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
