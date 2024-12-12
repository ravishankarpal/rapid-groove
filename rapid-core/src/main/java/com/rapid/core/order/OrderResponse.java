package com.rapid.core.order;

import com.rapid.core.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Data
@Builder
@Getter
@Setter
public class OrderResponse {

    private UUID orderId;
    private OrderStatus status;
    private String paymentTransactionId;
    private String paymentLink;
}
