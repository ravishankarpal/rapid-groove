package com.rapid.service;

import com.rapid.core.dto.OrderDto;
import com.rapid.core.entity.order.OrderDetails;
import jakarta.mail.MessagingException;

import java.util.List;

public interface OrderService {
    void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut) throws MessagingException;

    List<OrderDetails> getMyOrderDetails();
}
