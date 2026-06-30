package com.bookstore.web.commands;

import com.bookstore.model.SessionUser;
import com.bookstore.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CartUpdateCommand implements Command {

    private final CartService cartService;

    public CartUpdateCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            SessionUser user = CommandSupport.currentUser(req);
            long bookId = CommandSupport.requiredLong(req, "bookId");
            int qty = CommandSupport.optionalInt(req, "qty", 1);
            cartService.updateItem(user.getId(), bookId, qty);
            CommandSupport.redirect(req, resp, "/app/cart");
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/cart",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }
}
