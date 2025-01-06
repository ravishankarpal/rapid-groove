package com.rapid.core.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rapid.core.dto.cart.CartDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    @JsonProperty(value = "cart_details")
    private CartDetailResponse cartDetailResponse;

    @JsonProperty(value = "cf_order_id")
    private String cfOrderId;

    @JsonProperty(value = "created_at")
    private String createdAt;

    @JsonProperty(value = "customer_details")
    private CustomerDetails customerDetails;

    @JsonProperty(value = "entity")
    private String entity;

    @JsonProperty(value = "order_amount")
    private double order_amount;

    @JsonProperty(value = "order_currency")
    private String orderCurrency;
    @JsonProperty(value = "order_expiry_time")
    private String orderExpiryTime;

    @JsonProperty(value = "order_id")
    private String orderId;

    @JsonProperty(value = "order_meta")
    private OrderMetaData orderMetaData;

    @JsonProperty(value = "order_note")
    private String  orderNote;

    @JsonProperty(value = "order_status")
    private String  orderStatus;



    @JsonProperty(value = "payment_session_id")
    private String paymentSessionId;

}
