package com.bookstore.web.commands;

import com.bookstore.model.CartItem;
import com.bookstore.model.SessionUser;
import com.bookstore.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class CartViewCommand implements Command {

    private final CartService cartService;

    public CartViewCommand(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        SessionUser user = CommandSupport.currentUser(req);
        List<CartItem> items = cartService.listItems(user.getId());
        req.setAttribute("items", items);
        req.setAttribute("cartTotal", CartService.totalOf(items));
        return "cart/list";
    }
}
