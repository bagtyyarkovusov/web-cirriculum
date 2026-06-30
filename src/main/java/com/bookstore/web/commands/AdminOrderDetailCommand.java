package com.bookstore.web.commands;

import com.bookstore.model.Order;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class AdminOrderDetailCommand implements Command {

    private final OrderService orderService;

    public AdminOrderDetailCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long orderId;
        try {
            orderId = CommandSupport.requiredLong(req, "id");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Optional<Order> order = orderService.findAdminOrder(orderId);
        if (order.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        req.setAttribute("order", order.get());
        req.setAttribute("items", orderService.findItems(orderId));
        req.setAttribute("message", req.getParameter("message"));
        req.setAttribute("error", req.getParameter("error"));
        return "admin/order/detail";
    }
}
