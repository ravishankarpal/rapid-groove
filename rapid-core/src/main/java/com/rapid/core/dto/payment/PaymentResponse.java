package com.rapid.core.dto.payment;

import com.rapid.core.enums.PaymentStatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private String orderId;
    private String paymentSessionId;
    private String paymentLink;
    private PaymentStatusEnum status;
    private String errorMessage;
}
