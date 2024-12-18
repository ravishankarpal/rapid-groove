package com.rapid.core.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMetaData {


    @JsonProperty(value = "return_url")
    private String returnUrl;

    @JsonProperty(value = "notify_url")
    private String notifyUrl;

    @JsonProperty(value = "payment_methods")
    private String paymentMethods;

}
