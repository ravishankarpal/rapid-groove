package com.rapid.service;

import com.rapid.core.dto.OrderDto;
import com.rapid.core.entity.order.OrderDetail;
import com.rapid.core.entity.order.OrderDetails;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface OrderDetailsService {
    void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut) throws MessagingException, IOException;

    List<OrderDetails> getMyOrderDetails();

    OrderDetail createOrder(OrderDetail order);
}