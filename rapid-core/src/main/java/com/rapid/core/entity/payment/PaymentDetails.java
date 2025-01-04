package com.rapid.core.entity.payment;

import com.rapid.core.dto.payment.PaymentResponseDTO;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_details")

@Getter
@Setter
@NoArgsConstructor
public class PaymentDetails {

    @Id
    @Column(name = "transaction_id", unique = true, length = 100)
    private String transactionId;

    @Column(name = "payment_amount")
    private double paymentAmount;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false, length = 20)
    private String paymentStatus;

    @Column(name = "transaction_failed_message", length = 1000)
    private String transactionFailedMessage;


    @Column(name = "authenticate_status")
    private String authenticateStatus;


    @Column(name = "action", length = 255)
    private String action;

    @ManyToOne(fetch = FetchType.LAZY) // Many payments can be linked to one order
    @JoinColumn(name = "order_id", nullable = false) // Specifies the foreign key column
    private OrderDetails orderDetails;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PaymentDetails(PaymentResponseDTO paymentResponseDTO, OrderDetails orderDetails, User user) {
        this.transactionId = paymentResponseDTO.getTransactionId();
        this.paymentAmount = paymentResponseDTO.getPaymentAmount();
        this.paymentMethod = paymentResponseDTO.getPaymentMethod();
        this.paymentStatus = PaymentStatusEnum.CREATED.getStatus();
        this.action =paymentResponseDTO.getAction();
        this.authenticateStatus = "Unauthenticated";
        this.orderDetails= orderDetails;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
