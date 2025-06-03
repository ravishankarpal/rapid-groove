package com.rapid.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum OrderStatus {

    ORDER_PLACED("Order Placed"),
    NOT_SHIPPED_YET,
    SHIPPED(),
    PENDING(),
    PROCESSING(),
    DELIVERED(),
    CANCELLED(),
    ACTIVE();
    private String status;

}
