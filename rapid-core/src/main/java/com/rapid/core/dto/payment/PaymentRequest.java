package com.rapid.core.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest implements Serializable {


    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty(value = "payment_session_id")
    private String paymentSessionId;

    public void validate() {
        if (paymentSessionId == null || paymentSessionId.isEmpty()) {
            throw new IllegalArgumentException("Payment session ID cannot be null or empty.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Payment method cannot be null.");
        }
        paymentMethod.validate();
    }


}
