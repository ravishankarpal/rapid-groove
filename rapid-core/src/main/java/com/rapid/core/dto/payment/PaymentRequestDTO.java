package com.rapid.core.dto.payment;


import com.rapid.core.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

    private String orderId;
    private Double amount;
    private String  email;
    private String  phone;
    private String name;
    private String returnUrl;

    private PaymentMethod paymentMethod;

}