package com.rapid.service;

import com.rapid.core.dto.OrderDto;
import com.rapid.core.dto.orders.OrderExtend;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.dto.payment.PaymentRequest;
import com.rapid.core.entity.order.OrderDetails;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut) throws MessagingException, IOException;

    List<OrderDetails> getMyOrderDetails();

    OrderResponse createOrder(PaymentRequest paymentRequest) throws Exception;

    OrderResponse getOrder(String orderId) throws Exception;

    OrderResponse terminateOrder(String orderId) throws Exception;

    OrderResponse getOrderExtend(String orderId) throws Exception;

    OrderResponse updateOrderExtend(String orderId, OrderExtend orderExtend ) throws Exception;

    public HttpHeaders getCashFreeHeaders() throws Exception;

}
