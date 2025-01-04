package com.rapid.core.dto.orders;

import com.rapid.core.dto.delivery.ShipmentDetails;
import com.rapid.core.dto.delivery.TrackingInfo;
import com.rapid.core.dto.product.ProductItem;
import com.rapid.core.entity.UserAddress;
import com.rapid.core.entity.delivery.DeliverInfoDetails;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.entity.order.OrderProductDetails;
import com.rapid.core.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private String paymentMethod;
    private double shippingCharge;
    private double cashOnDelivery;
    private double totalAmount;
    private UserAddress shippingAddress;
    private List<ProductItem> items = new ArrayList<>();
    private String deliveryDate;
    private String returnWindowCloseDate;
    private TrackingInfo trackingInfo;
    public OrderResponse(OrderDetails orders) {
        this.orderId = orders.getOrderId();
        this.orderDate = String.valueOf(orders.getCreatedAt());
        this.orderStatus = orders.getOrderStatus();
        this.totalAmount = orders.getOrderAmount();
        this.shippingAddress = orders.getUserAddress();
        this.shippingCharge =orders.getShippingCharge();
        for (OrderProductDetails orderProductDetails:  orders.getOrderProducts()){
            ProductItem item = new ProductItem(orderProductDetails);
            this.items.add(item);
        }

        this.paymentMethod = orders.getOrderMeta().getPaymentMethods();
        this.deliveryDate = LocalDateTime.now().toString();
        this.returnWindowCloseDate = LocalDateTime.now().toString();
        this.trackingInfo= new TrackingInfo(orders.getDeliveryInfo());

    }
}
