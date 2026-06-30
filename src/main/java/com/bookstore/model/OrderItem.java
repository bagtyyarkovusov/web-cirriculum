package com.bookstore.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable {

    private long id;
    private long orderId;
    private long bookId;
    private String titleSnapshot;
    private BigDecimal priceSnapshot;
    private int qty;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getOrderId() { return orderId; }
    public void setOrderId(long orderId) { this.orderId = orderId; }

    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }

    public String getTitleSnapshot() { return titleSnapshot; }
    public void setTitleSnapshot(String titleSnapshot) { this.titleSnapshot = titleSnapshot; }

    public BigDecimal getPriceSnapshot() { return priceSnapshot; }
    public void setPriceSnapshot(BigDecimal priceSnapshot) { this.priceSnapshot = priceSnapshot; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public BigDecimal getLineTotal() {
        BigDecimal price = priceSnapshot == null ? BigDecimal.ZERO : priceSnapshot;
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
