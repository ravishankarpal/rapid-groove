package com.rapid.core.entity.order;

import com.rapid.core.entity.User;
import com.rapid.core.entity.UserAddress;
import com.rapid.core.enums.OrderStatus;
import com.rapid.core.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private UserAddress shippingAddress;

    @OneToOne
    @JoinColumn(name = "billing_address_id", nullable = false)
    private UserAddress billingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "sub_total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotalAmount;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_transaction_id")
    private String paymentTransactionId;

    @OneToMany(mappedBy = "orderDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Embedded
    private OrderTracking orderTracking;

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus newStatus) {
        switch (current) {
            case PENDING:
                return newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELLED;
            case PROCESSING:
                return newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELLED;
            case SHIPPED:
                return newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.RETURNED;
            default:
                return false;
        }
    }

    public void updateOrderStatus(OrderStatus newStatus) {
        if (isValidStatusTransition(this.orderStatus, newStatus)) {
            this.orderStatus = newStatus;
        } else {
            throw new IllegalStateException("Invalid order status transition");
        }
    }
}
