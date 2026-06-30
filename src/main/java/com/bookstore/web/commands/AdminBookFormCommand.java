package com.bookstore.web.commands;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class AdminBookFormCommand implements Command {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final boolean edit;

    public AdminBookFormCommand(BookService bookService, CategoryService categoryService, boolean edit) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.edit = edit;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Book book;
        if (edit) {
            long id;
            try {
                id = CommandSupport.requiredLong(req, "id");
            } catch (IllegalArgumentException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
            Optional<Book> existing = bookService.findBook(id);
            if (existing.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }
            book = existing.get();
        } else {
            book = new Book();
            book.setStatus("ON");
        }

        req.setAttribute("book", book);
        req.setAttribute("categories", categoryService.listCategories());
        return "admin/book/form";
    }
}
