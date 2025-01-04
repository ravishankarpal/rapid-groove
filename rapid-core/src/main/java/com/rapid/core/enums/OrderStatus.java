package com.rapid.core.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum OrderStatus {

    ORDER_PLACED(),
    NOT_SHIPPED_YET,
    SHIPPED(),
    PENDING(),
    PROCESSING(),
    DELIVERED(),
    CANCELLED(),
    ACTIVE();

}
