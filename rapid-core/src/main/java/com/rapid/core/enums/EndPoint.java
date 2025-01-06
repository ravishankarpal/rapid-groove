package com.rapid.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EndPoint {
    PG_ORDERS("/pg/orders"),
    SUBMIT_OTP("/pg/orders/pay/authenticate/");

    public String endPoint;


}
