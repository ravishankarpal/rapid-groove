package com.rapid.core.entity;


import com.rapid.core.entity.order.OrderDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "payments")

public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_id",unique = true)
    private String transactionId;

    @Column(name = "payment_method",nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "amount",nullable = false)
    private Double amount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetails order;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.transactionId = "TXN" + System.currentTimeMillis();
    }
}
