package com.rapid.service;

import com.rapid.core.dto.OrderDto;
import com.rapid.core.entity.order.OrderDetails;

import java.util.List;

public interface OrderService {
    void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut);

    List<OrderDetails> getMyOrderDetails();
}
