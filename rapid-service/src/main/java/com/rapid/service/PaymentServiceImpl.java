package com.rapid.service;


import com.rapid.core.dto.PaymentRequestDTO;
import com.rapid.core.dto.PaymentResponseDTO;
import com.rapid.core.entity.Payment;
import com.rapid.core.enums.PaymentStatusEnum;
import com.rapid.core.exception.InvalidPaymentException;
import com.rapid.core.exception.PaymentProcessingException;
import com.rapid.dao.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        try {
            validatePaymentRequest(request);
            Payment payment = new Payment();
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setAmount(request.getAmount());
            payment.setDiscount(request.getDiscount());
            payment.setStatus(PaymentStatusEnum.PENDING.getStatus());
            payment = paymentRepository.saveAndFlush(payment);
            String paymentStatus = processPaymentByMethod(payment);
            payment.setStatus(paymentStatus);
            payment = paymentRepository.save(payment);

            return new PaymentResponseDTO(true,
                    "Payment processed successfully",
                    payment.getTransactionId(),
                    payment.getStatus());

        } catch (Exception e) {
            throw new PaymentProcessingException("Payment processing failed: " + e.getMessage());
        }
    }


    private void validatePaymentRequest(PaymentRequestDTO request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new InvalidPaymentException("Invalid payment amount");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().trim().isEmpty()) {
            throw new InvalidPaymentException("Payment method is required");
        }
    }



    private String processPaymentByMethod(Payment payment) {
        switch (payment.getPaymentMethod().toLowerCase()) {
            case "upi":
                return processUPIPayment(payment);
            case "wallet":
                return processWalletPayment(payment);
            case "cod":
                return processPayLaterPayment(payment);
            case "cards":
                return processCardPayment(payment);
            default:
                throw new InvalidPaymentException("Unsupported payment method");
        }
    }



    private String processUPIPayment(Payment payment) {

        try {
            Thread.sleep(1000);
            return PaymentStatusEnum.SUCCESS.getStatus();
        } catch (InterruptedException e) {
            throw new PaymentProcessingException("UPI payment processing failed");
        }
    }

    private String processWalletPayment(Payment payment) {
        try {
            Thread.sleep(1000);
            return PaymentStatusEnum.SUCCESS.getStatus();
        } catch (InterruptedException e) {
            throw new PaymentProcessingException("Wallet payment processing failed");
        }
    }


    private String processPayLaterPayment(Payment payment) {

        try {
            Thread.sleep(1000);
            return PaymentStatusEnum.COD.getStatus();
        } catch (InterruptedException e) {
            throw new PaymentProcessingException("Pay later processing failed");
        }
    }

    private String processCardPayment(Payment payment) {
        try {
            Thread.sleep(1000);
            return PaymentStatusEnum.SUCCESS.getStatus();
        } catch (InterruptedException e) {
            throw new PaymentProcessingException("Card payment processing failed");
        }
    }

}
