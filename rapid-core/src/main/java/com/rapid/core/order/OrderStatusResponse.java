package com.rapid.core.order;


import com.rapid.core.entity.UserAddress;
import com.rapid.core.entity.order.OrderTracking;
import com.rapid.core.enums.OrderStatus;
import com.rapid.core.enums.PaymentStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
public class OrderStatusResponse {

    private UUID orderId;
    private OrderStatus orderStatus;
    private PaymentStatusEnum paymentStatus;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalAmount;
    private UserAddress shippingAddress;
    private OrderTracking orderTracking;
}
