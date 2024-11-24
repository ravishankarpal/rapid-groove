package com.rapid.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentResponseDTO {

    private boolean success;
    private String message;
    private String transactionId;
    private String status;

    public PaymentResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
