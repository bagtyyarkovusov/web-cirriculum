package com.bookstore.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStatusTest {

    @Test
    void pendingOrdersCanBeCancelledBeforeShipment() {
        assertTrue(OrderStatus.PENDING_PAY.canCancel());
        assertTrue(OrderStatus.PENDING_SHIP.canCancel());
        assertFalse(OrderStatus.SHIPPED.canCancel());
        assertFalse(OrderStatus.COMPLETED.canCancel());
        assertFalse(OrderStatus.CANCELLED.canCancel());
    }

    @Test
    void onlyShippedOrdersCanBeConfirmed() {
        assertFalse(OrderStatus.PENDING_PAY.canConfirmReceipt());
        assertFalse(OrderStatus.PENDING_SHIP.canConfirmReceipt());
        assertTrue(OrderStatus.SHIPPED.canConfirmReceipt());
        assertFalse(OrderStatus.COMPLETED.canConfirmReceipt());
        assertFalse(OrderStatus.CANCELLED.canConfirmReceipt());
    }

    @Test
    void fromDbParsesKnownStatusAndRejectsUnknownValue() {
        assertEquals(OrderStatus.PENDING_SHIP, OrderStatus.fromDb("PENDING_SHIP"));
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.fromDb("UNKNOWN"));
    }
}
