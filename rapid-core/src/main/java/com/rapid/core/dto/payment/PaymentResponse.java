package com.rapid.core.dto.payment;

import com.rapid.core.enums.PaymentStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class PaymentResponse {

    private String orderId;
    private String paymentSessionId;
    private String paymentLink;
    private PaymentStatusEnum status;
    private String errorMessage;
}
