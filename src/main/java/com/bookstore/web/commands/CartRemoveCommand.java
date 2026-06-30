package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CartRemoveCommand implements Command {

    private final CartService cartService;

    public CartRemoveCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            SessionUser user = CommandSupport.currentUser(req);
            long bookId = CommandSupport.requiredLong(req, "bookId");
            cartService.removeItem(user.getId(), bookId);
            CommandSupport.redirect(req, resp, "/app/cart");
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/cart",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
