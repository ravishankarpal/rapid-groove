package com.rapid.service.orders;

import com.rapid.core.entity.order.OrderDetail;
import com.rapid.core.order.OrderRequest;

import java.util.UUID;

public interface OrderService {

    OrderDetail createOrder(OrderRequest orderRequest);


    OrderDetail updateOrder(OrderDetail orderDetail);

    OrderDetail getOrderById(UUID orderId);
}
