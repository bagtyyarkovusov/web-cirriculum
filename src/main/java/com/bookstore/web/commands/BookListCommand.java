package com.bookstore.web.commands;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Lists on-sale books — the tracer-bullet action proving
 * Servlet → Service → DAO → JDBC → JSP end to end.
 */
public class BookListCommand implements Command {

    private final BookService bookService;

    public BookListCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        List<Book> books = bookService.listOnSale();
        req.setAttribute("books", books);
        return "book/list";
    }
}
