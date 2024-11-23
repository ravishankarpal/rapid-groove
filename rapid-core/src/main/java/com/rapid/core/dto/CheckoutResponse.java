package com.rapid.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CheckoutResponse {

    private List<CheckoutItemResponse> checkoutItemResponses;
    private BigDecimal totalAmount;
    private Double discountAmount;
    private String deliveryFee;

}
