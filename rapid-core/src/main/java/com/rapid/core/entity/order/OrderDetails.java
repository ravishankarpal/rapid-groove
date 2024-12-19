package com.rapid.core.entity.order;

import com.rapid.core.dto.cart.CartDetail;
import com.rapid.core.dto.orders.CustomerDetails;
import com.rapid.core.dto.orders.OrderMetaData;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.dto.payment.PaymentRequest;

import com.rapid.core.entity.User;
import com.rapid.core.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


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

    @Embedded
    private CustomerDetails customerDetails;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "order_id")
//    private List<CartItem> cartItems = new ArrayList<>();

    @Embedded
    @AssociationOverride(
            name = "orderCartItems"

    )
    private OrderCartDetails cartDetails;

    @Embedded
    private OrderMetaData orderMeta;

    @Column(name = "order_amount")
    private double orderAmount;

    @Column(name = "order_currency", length = 10)
    private String orderCurrency;

    @Column(name = "order_note", length = 255)
    private String orderNote;

    @Column(name = "order_status", length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "payment_session_id", length = 1000)
    private String paymentSessionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "order_expiry_time")
    private String orderExpiryTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public OrderDetails(OrderResponse orderResponse, PaymentRequest paymentRequest){
        this.orderId = orderResponse.getOrderId();


       // this.cartDetails = paymentRequest.getCartDetails();
        this.cfOrderId = orderResponse.getCfOrderId();
        this.customerDetails = orderResponse.getCustomerDetails();
        this.orderAmount = orderResponse.getOrder_amount();
        this.orderCurrency = orderResponse.getOrderCurrency();
        this.orderExpiryTime = orderResponse.getOrderExpiryTime();
        this.orderMeta = orderResponse.getOrderMetaData();
        this.orderNote = orderResponse.getOrderNote();
        this.orderStatus = OrderStatus.valueOf(orderResponse.getOrderStatus());
        this.paymentSessionId = orderResponse.getPaymentSessionId();
    }

}
