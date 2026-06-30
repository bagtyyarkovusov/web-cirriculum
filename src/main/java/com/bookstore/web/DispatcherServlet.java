package com.bookstore.web;

import com.bookstore.service.BookService;
import com.bookstore.service.CartService;
import com.bookstore.service.CategoryService;
import com.bookstore.service.AuditService;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import com.bookstore.web.commands.AdminAuditListCommand;
import com.bookstore.web.commands.AdminBookFormCommand;
import com.bookstore.web.commands.AdminBookListCommand;
import com.bookstore.web.commands.AdminBookSaveCommand;
import com.bookstore.web.commands.AdminBookStatusCommand;
import com.bookstore.web.commands.AdminCategoryDeleteCommand;
import com.bookstore.web.commands.AdminCategoryListCommand;
import com.bookstore.web.commands.AdminCategorySaveCommand;
import com.bookstore.web.commands.AdminOrderDetailCommand;
import com.bookstore.web.commands.AdminOrderListCommand;
import com.bookstore.web.commands.AdminOrderShipCommand;
import com.bookstore.web.commands.AdminUserListCommand;
import com.bookstore.web.commands.AdminUserResetPasswordCommand;
import com.bookstore.web.commands.AdminUserStatusCommand;
import com.bookstore.web.commands.BookListCommand;
import com.bookstore.web.commands.CartAddCommand;
import com.bookstore.web.commands.CartRemoveCommand;
import com.bookstore.web.commands.CartUpdateCommand;
import com.bookstore.web.commands.CartViewCommand;
import com.bookstore.web.commands.CheckoutCommand;
import com.bookstore.web.commands.Command;
import com.bookstore.web.commands.LoginFormCommand;
import com.bookstore.web.commands.LoginSubmitCommand;
import com.bookstore.web.commands.LogoutCommand;
import com.bookstore.web.commands.OrderCancelCommand;
import com.bookstore.web.commands.OrderConfirmCommand;
import com.bookstore.web.commands.OrderDetailCommand;
import com.bookstore.web.commands.OrderListCommand;
import com.bookstore.web.commands.RegisterFormCommand;
import com.bookstore.web.commands.RegisterSubmitCommand;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Front controller mapped to {@code /app/*}. Routes the path after {@code /app}
 * to a {@link Command} and forwards to the returned JSP view. Cross-cutting
 * filters (Auth / RBAC / Audit) will hang off this dispatcher in later slices.
 */
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Command> getRoutes = new HashMap<>();
    private final Map<String, Command> postRoutes = new HashMap<>();

    @Override
    public void init() {
        BookService bookService = new BookService();
        CartService cartService = new CartService();
        CategoryService categoryService = new CategoryService();
        AuditService auditService = createAuditService();
        OrderService orderService = new OrderService();
        UserService userService = new UserService();
        getRoutes.put("/books", new BookListCommand(bookService));
        getRoutes.put("/cart", new CartViewCommand(cartService));
        getRoutes.put("/orders", new OrderListCommand(orderService));
        getRoutes.put("/orders/detail", new OrderDetailCommand(orderService));
        getRoutes.put("/login", new LoginFormCommand());
        getRoutes.put("/register", new RegisterFormCommand());
        getRoutes.put("/logout", new LogoutCommand(auditService));
        Command adminDashboard = (req, resp) -> "admin/dashboard";
        getRoutes.put("/admin", adminDashboard);
        getRoutes.put("/admin/", adminDashboard);
        getRoutes.put("/admin/audit", new AdminAuditListCommand(auditService));
        getRoutes.put("/admin/audit/", new AdminAuditListCommand(auditService));
        getRoutes.put("/admin/books", new AdminBookListCommand(bookService));
        getRoutes.put("/admin/books/new", new AdminBookFormCommand(bookService, categoryService, false));
        getRoutes.put("/admin/books/edit", new AdminBookFormCommand(bookService, categoryService, true));
        getRoutes.put("/admin/categories", new AdminCategoryListCommand(categoryService));
        getRoutes.put("/admin/orders", new AdminOrderListCommand(orderService));
        getRoutes.put("/admin/orders/detail", new AdminOrderDetailCommand(orderService));
        getRoutes.put("/admin/users", new AdminUserListCommand(userService));

        postRoutes.put("/login", new LoginSubmitCommand(userService, auditService));
        postRoutes.put("/register", new RegisterSubmitCommand(userService, auditService));
        postRoutes.put("/cart/add", new CartAddCommand(cartService));
        postRoutes.put("/cart/update", new CartUpdateCommand(cartService));
        postRoutes.put("/cart/remove", new CartRemoveCommand(cartService));
        postRoutes.put("/checkout", new CheckoutCommand(orderService, auditService));
        postRoutes.put("/orders/cancel", new OrderCancelCommand(orderService, auditService));
        postRoutes.put("/orders/confirm", new OrderConfirmCommand(orderService, auditService));
        postRoutes.put("/admin/books/save", new AdminBookSaveCommand(bookService, auditService));
        postRoutes.put("/admin/books/status", new AdminBookStatusCommand(bookService, auditService));
        postRoutes.put("/admin/categories/save", new AdminCategorySaveCommand(categoryService, auditService));
        postRoutes.put("/admin/categories/delete", new AdminCategoryDeleteCommand(categoryService, auditService));
        postRoutes.put("/admin/orders/ship", new AdminOrderShipCommand(orderService, auditService));
        postRoutes.put("/admin/users/status", new AdminUserStatusCommand(userService, auditService));
        postRoutes.put("/admin/users/reset-password", new AdminUserResetPasswordCommand(userService, auditService));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            path = "/books";
        }
        Command command = commandFor(req.getMethod(), path);
        if (command == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String view = command.execute(req, resp);
        if (view != null) {
            req.getRequestDispatcher("/WEB-INF/views/" + view + ".jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        doGet(req, resp);
    }

    private Command commandFor(String method, String path) {
        return "POST".equalsIgnoreCase(method) ? postRoutes.get(path) : getRoutes.get(path);
    }

    protected AuditService createAuditService() {
        return new AuditService();
    }
}
