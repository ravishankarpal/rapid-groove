package com.rapid.core.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum OrderStatus {

    ORDER_PLACED(),
    PENDING(),
    PROCESSING(),
    SHIPPED(),
    DELIVERED(),
    CANCELLED(),
    ACTIVE();

    //private final String status;

//    OrderStatus(String status){
//        this.status = status;
//    }

}
