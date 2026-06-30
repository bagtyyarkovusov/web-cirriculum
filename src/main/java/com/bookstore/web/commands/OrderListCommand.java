package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OrderListCommand implements Command {

    private final OrderService orderService;

    public OrderListCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        SessionUser user = CommandSupport.currentUser(req);
        req.setAttribute("orders", orderService.listOrders(user.getId()));
        return "order/list";
    }
}
