package com.rapid.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EndPoint {
    PG_ORDERS("/pg/orders"),
    AUTHENTICATE("/pg/orders/pay/authenticate/"),
    PG_ORDERS_SESSION("/pg/orders/sessions") ;

    public String endPoint;


}
