package com.bookstore.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cart row joined with the current book data needed by cart and checkout views.
 */
public class CartItem implements Serializable {

    private long id;
    private long userId;
    private long bookId;
    private int qty;
    private String bookTitle;
    private BigDecimal bookPrice;
    private int bookStock;
    private String bookStatus;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public BigDecimal getBookPrice() { return bookPrice; }
    public void setBookPrice(BigDecimal bookPrice) { this.bookPrice = bookPrice; }

    public int getBookStock() { return bookStock; }
    public void setBookStock(int bookStock) { this.bookStock = bookStock; }

    public String getBookStatus() { return bookStatus; }
    public void setBookStatus(String bookStatus) { this.bookStatus = bookStatus; }

    public BigDecimal getLineTotal() {
        BigDecimal price = bookPrice == null ? BigDecimal.ZERO : bookPrice;
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
