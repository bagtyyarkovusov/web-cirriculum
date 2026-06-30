package com.bookstore.web.commands;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

public class AdminBookSaveCommand implements Command {

    private final BookService bookService;

    public AdminBookSaveCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Book book = new Book();
            Long id = CommandSupport.optionalLong(req, "id");
            book.setId(id == null ? 0 : id);
            book.setTitle(CommandSupport.optionalString(req, "title"));
            book.setAuthor(CommandSupport.optionalString(req, "author"));
            book.setPublisher(CommandSupport.optionalString(req, "publisher"));
            book.setIsbn(CommandSupport.optionalString(req, "isbn"));
            book.setPrice(parsePrice(req));
            book.setStock(CommandSupport.optionalInt(req, "stock", 0));
            book.setCategoryId(CommandSupport.optionalLong(req, "categoryId"));
            book.setCoverPath(CommandSupport.optionalString(req, "coverPath"));
            book.setIntro(CommandSupport.optionalString(req, "intro"));
            book.setStatus(CommandSupport.optionalString(req, "status"));
            bookService.save(book);
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/books", "message", "图书已保存。");
        } catch (RuntimeException e) {
            CommandSupport.redirectWithMessage(req, resp, "/app/admin/books",
                    "error", CommandSupport.operationError(e));
        }
        return null;
    }

    private BigDecimal parsePrice(HttpServletRequest req) {
        try {
            return new BigDecimal(CommandSupport.optionalString(req, "price"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("价格参数错误。", e);
        }
    }
}
