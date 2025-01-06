package com.rapid.core.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponseDTO {

    private String action;

    @JsonProperty("cf_payment_id")
    private String transactionId;

    @JsonProperty("data")
    private PaymentResponseData paymentResponseData;

    @JsonProperty("payment_amount")
    private double paymentAmount;

    @JsonProperty("payment_method")
    private String paymentMethod;

}
