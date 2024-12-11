package com.rapid.core.enums;

import lombok.Getter;

@Getter
public enum PaymentStatusEnum {


    INITIATED("Initiated"),
    SUCCESS("Success"),
    PENDING("Pending"),
    COD("cod"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    private final String status;

    PaymentStatusEnum(String status){
        this.status = status;
    }
}
