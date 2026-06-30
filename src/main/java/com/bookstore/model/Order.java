package com.bookstore.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order implements Serializable {

    private long id;
    private String orderNo;
    private long userId;
    private BigDecimal total;
    private OrderStatus status;
    private String receiverSnapshot;
    private String trackingNo;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getStatusValue() { return status == null ? "" : status.name(); }

    public boolean isCancellable() { return status != null && status.canCancel(); }

    public boolean isConfirmable() { return status != null && status.canConfirmReceipt(); }

    public String getReceiverSnapshot() { return receiverSnapshot; }
    public void setReceiverSnapshot(String receiverSnapshot) { this.receiverSnapshot = receiverSnapshot; }

    public String getTrackingNo() { return trackingNo; }
    public void setTrackingNo(String trackingNo) { this.trackingNo = trackingNo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public LocalDateTime getShippedAt() { return shippedAt; }
    public void setShippedAt(LocalDateTime shippedAt) { this.shippedAt = shippedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
