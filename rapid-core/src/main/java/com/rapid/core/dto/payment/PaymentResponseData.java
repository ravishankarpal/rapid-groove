package com.rapid.core.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponseData {

    private String url;
    private String payload;
    @JsonProperty("content_type")
    private String contentType;

    private String method;
}
