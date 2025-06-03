package com.rapid.service;

import com.rapid.core.dto.OrderDto;
import com.rapid.core.dto.orders.OrderExtend;
import com.rapid.core.dto.orders.CashFreeOrderResponse;
import com.rapid.core.dto.orders.OrderRequest;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.entity.delivery.DeliverInfoDetails;
import com.rapid.core.enums.OrderStatus;
import com.rapid.service.exception.TokenExpiredException;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

import java.io.IOException;

public interface OrderService {
    void placeOrder(OrderDto orderDto, boolean isSingleCartCheckOut) throws MessagingException, IOException;


    CashFreeOrderResponse createOrder(OrderRequest paymentRequest) throws Exception;

    CashFreeOrderResponse getOrder(String orderId) throws Exception;

    CashFreeOrderResponse terminateOrder(String orderId) throws Exception;

    CashFreeOrderResponse getOrderExtend(String orderId) throws Exception;

    CashFreeOrderResponse updateOrderExtend(String orderId, OrderExtend orderExtend ) throws Exception;

    public HttpHeaders getCashFreeHeaders() throws Exception;

    void sendOrderConfirmationEmail(String orderId) throws MessagingException, IOException;

    Page<OrderResponse> getOrders(String period, OrderStatus status, int page, int size);

    OrderResponse getOrderDetailsById(String id) throws TokenExpiredException;

    DeliverInfoDetails trackOrder(String orderId);
}
