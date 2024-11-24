package com.rapid.core.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

    private String paymentMethod;
    private Double amount;
    private Double discount;

}
