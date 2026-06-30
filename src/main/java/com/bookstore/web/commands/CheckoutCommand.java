package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CheckoutCommand implements Command {

    private final OrderService orderService;

    public CheckoutCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            SessionUser user = CommandSupport.currentUser(req);
            long orderId = orderService.checkout(user.getId());
            CommandSupport.redirect(req, resp, "/app/orders/detail?id=" + orderId);
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/cart",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
