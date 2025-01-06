package com.rapid.core.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticatePaymentResponse {

    @JsonProperty("action")
    private  String action;

    @JsonProperty("authenticate_status")
    private String authenticateStatus;

    @JsonProperty("cf_payment_id")
    private String transactionId;

    @JsonProperty("payment_message")
    private String paymentMessage;

}
