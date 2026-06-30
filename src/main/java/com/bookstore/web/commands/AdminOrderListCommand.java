package com.bookstore.web.commands;

import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminOrderListCommand implements Command {

    private final OrderService orderService;

    public AdminOrderListCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("orders", orderService.listAdminOrders());
        req.setAttribute("message", req.getParameter("message"));
        req.setAttribute("error", req.getParameter("error"));
        return "admin/order/list";
    }
}
