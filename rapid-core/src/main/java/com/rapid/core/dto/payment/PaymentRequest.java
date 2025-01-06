package com.rapid.core.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rapid.core.dto.cart.CartDetail;
import com.rapid.core.dto.orders.CustomerDetails;
import com.rapid.core.dto.orders.OrderMetaData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @JsonProperty(value = "cart_details")
    private CartDetail cartDetails;

    @JsonProperty(value = "customer_details")
    private CustomerDetails customerDetails;

    @JsonProperty(value = "order_meta")
    private OrderMetaData orderMetaData;

    @JsonProperty(value = "order_id")
    private String orderId;

    @Min(1)
    @JsonProperty(value = "order_amount")
    private double orderAmount;

    @JsonProperty(value = "order_currency")
    private String orderCurrency;

    @JsonProperty(value = "order_expiry_time")
    private String orderExpiryTime;

    @JsonProperty(value = "order_note")
    private String orderNote;

}
