package com.bookstore.web.commands;

import com.bookstore.service.AuditService;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminOrderShipCommand implements Command {

    private final OrderService orderService;
    private final AuditService auditService;

    public AdminOrderShipCommand(OrderService orderService, AuditService auditService) {
        this.orderService = orderService;
        this.auditService = auditService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long orderId = 0;
        try {
            orderId = CommandSupport.requiredLong(req, "id");
            String trackingNo = CommandSupport.optionalString(req, "trackingNo");
            boolean shipped = orderService.shipOrder(orderId, trackingNo);
            String path = "/app/admin/orders/detail?id=" + orderId;
            if (shipped) {
                CommandSupport.audit(req, auditService, "ORDER_SHIP",
                        "orderId=" + orderId + ",trackingNoSet=true");
                CommandSupport.redirectWithMessage(req, resp, path, "message", "订单已发货。");
            } else {
                CommandSupport.redirectWithMessage(req, resp, path, "error", "只有待发货订单可以发货。");
            }
        } catch (RuntimeException e) {
            String path = orderId > 0 ? "/app/admin/orders/detail?id=" + orderId : "/app/admin/orders";
            CommandSupport.redirectWithMessage(req, resp, path,
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
