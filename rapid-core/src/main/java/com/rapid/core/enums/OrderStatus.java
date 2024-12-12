package com.rapid.core.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    ORDER_PLACED("Placed"),
    PENDING("pending"),
    PROCESSING("Pending"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    RETURNED("Returned"),
    CANCELLED("Cancelled"),
    PAYMENT_PENDING("Payment Pending");

    private final String status;

    OrderStatus(String status){
        this.status = status;
    }

}
