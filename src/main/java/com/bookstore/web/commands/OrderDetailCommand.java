package com.bookstore.web.commands;

import com.bookstore.model.Order;
import com.bookstore.model.SessionUser;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class OrderDetailCommand implements Command {

    private final OrderService orderService;

    public OrderDetailCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SessionUser user = CommandSupport.currentUser(req);
        long orderId;
        try {
            orderId = CommandSupport.requiredLong(req, "id");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        Optional<Order> order = orderService.findOrder(user.getId(), orderId);
        if (order.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        req.setAttribute("order", order.get());
        req.setAttribute("items", orderService.findItems(orderId));
        return "order/detail";
    }
}
