package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.AuditService;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class OrderCancelCommand implements Command {

    private final OrderService orderService;
    private final AuditService auditService;

    public OrderCancelCommand(OrderService orderService, AuditService auditService) {
        this.orderService = orderService;
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long orderId = CommandSupport.requiredLong(req, "id");
            SessionUser user = CommandSupport.currentUser(req);
            boolean cancelled = orderService.cancel(user.getId(), orderId);
            if (cancelled) {
                CommandSupport.audit(req, auditService, "ORDER_CANCEL", "orderId=" + orderId);
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
