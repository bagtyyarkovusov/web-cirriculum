package com.bookstore.service;

import com.bookstore.dao.BookDao;
import com.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Book service layer. Thin for now — the tracer-bullet slice only lists on-sale books.
 */
public class BookService {

    private final BookDao bookDao = new BookDao();

    public List<Book> listOnSale() {
        return bookDao.findOnSale();
    }

    public List<Book> listAdminBooks() {
        return bookDao.findAll();
    }

    public Optional<Book> findBook(long id) {
        return bookDao.findById(id);
    }

    public long save(Book book) {
        normalizeAndValidate(book);
        if (book.getId() > 0) {
            if (!bookDao.update(book)) {
                throw new IllegalArgumentException("图书不存在。");
            }
            return book.getId();
        }
        return bookDao.insert(book);
    }

    public boolean updateStatus(long id, String status) {
        return bookDao.updateStatus(id, normalizeStatus(status));
    }

    private void normalizeAndValidate(Book book) {
        book.setTitle(required(book.getTitle(), "书名不能为空。"));
        book.setAuthor(emptyToNull(book.getAuthor()));
        book.setPublisher(emptyToNull(book.getPublisher()));
        book.setIsbn(emptyToNull(book.getIsbn()));
        book.setCoverPath(emptyToNull(book.getCoverPath()));
        book.setIntro(emptyToNull(book.getIntro()));
        book.setStatus(normalizeStatus(book.getStatus()));
        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("价格不能为负数。");
        }
        if (book.getStock() < 0) {
            throw new IllegalArgumentException("库存不能为负数。");
        }
    }

    private String normalizeStatus(String status) {
        String normalized = status == null ? "" : status.trim().toUpperCase();
        if (!"ON".equals(normalized) && !"OFF".equals(normalized)) {
            throw new IllegalArgumentException("图书状态参数错误。");
        }
        return normalized;
    }

    private String required(String value, String message) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private String emptyToNull(String value) {
        String normalized = value == null ? "" : value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}
