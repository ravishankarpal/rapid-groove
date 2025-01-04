package com.rapid.core.entity.order;

import com.rapid.core.dto.cart.CartDetail;
import com.rapid.core.dto.cart.CartItems;
import com.rapid.core.dto.orders.OrderMetaData;
import com.rapid.core.dto.orders.CashFreeOrderResponse;
import com.rapid.core.dto.orders.OrderRequest;

import com.rapid.core.entity.User;
import com.rapid.core.entity.UserAddress;
import com.rapid.core.entity.delivery.DeliverInfoDetails;
import com.rapid.core.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "order_details")
@NoArgsConstructor
public class OrderDetails {
    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String orderId;

    @Column(name = "cf_order_id", length = 50)
    private String cfOrderId;

    @ElementCollection
    @CollectionTable(
            name = "order_product_details",
            joinColumns = @JoinColumn(name = "order_id")
    )
    private List<OrderProductDetails> orderProducts = new ArrayList<>();


    @Embedded
    private OrderMetaData orderMeta;

    @Column(name = "order_amount")
    private double orderAmount;

    @Column(name = "order_currency", length = 10)
    private String orderCurrency;

    @Column(name = "order_note", length = 255)
    private String orderNote;

    @Column(name = "order_status", length = 40)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "payment_session_id", length = 1000)
    private String paymentSessionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "order_expiry_time")
    private String orderExpiryTime;

    @Column(name = "shipping_charge")
    private double shippingCharge;

    @ManyToOne
    @JoinColumn(name ="user_address_id")
    private UserAddress userAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_info_id")
    private DeliverInfoDetails deliveryInfo;


    public OrderDetails(CashFreeOrderResponse orderResponse, OrderRequest paymentRequest){
        this.orderId = orderResponse.getOrderId();
        this.cfOrderId = orderResponse.getCfOrderId();
        this.orderAmount = orderResponse.getOrder_amount();
        this.orderCurrency = orderResponse.getOrderCurrency();
        this.orderExpiryTime = orderResponse.getOrderExpiryTime();
        this.orderMeta = orderResponse.getOrderMetaData();
        this.orderNote = orderResponse.getOrderNote();
        this.orderStatus = OrderStatus.valueOf(orderResponse.getOrderStatus());
        this.paymentSessionId = orderResponse.getPaymentSessionId();
        CartDetail cartDetails = paymentRequest.getCartDetails();
        if(cartDetails.getShippingCharge() ==null) {
            this.shippingCharge = 0.0;
        }else{
            this.shippingCharge = cartDetails.getShippingCharge();
        }
        List<CartItems> cartItems = cartDetails.getCartItems();
        for(CartItems items : cartItems){
            OrderProductDetails orderProductDetails = new OrderProductDetails(items);
            orderProducts.add(orderProductDetails);
        }

    }

}
