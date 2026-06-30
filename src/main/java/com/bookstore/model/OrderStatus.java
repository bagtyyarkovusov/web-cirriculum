package com.bookstore.model;

public enum OrderStatus {
    PENDING_PAY,
    PENDING_SHIP,
    SHIPPED,
    COMPLETED,
    CANCELLED;

    public boolean canCancel() {
        return this == PENDING_PAY || this == PENDING_SHIP;
    }

    public boolean canConfirmReceipt() {
        return this == SHIPPED;
    }

    public static OrderStatus fromDb(String value) {
        for (OrderStatus status : values()) {
            if (status.name().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown order status: " + value);
    }
}
