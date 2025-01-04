package com.rapid.service;

import com.rapid.core.dto.PaymentRequestDTO;
import com.rapid.core.dto.payment.AuthenticatePaymentResponse;
import com.rapid.core.dto.payment.PaymentRequest;
import com.rapid.core.dto.payment.PaymentResponseDTO;
import com.rapid.core.dto.payment.AuthenticatePaymentRequest;
import org.springframework.http.ResponseEntity;

public interface PaymentService {

    PaymentResponseDTO processPayment(PaymentRequest request);

    AuthenticatePaymentResponse authenticatePayment(String paymentId, AuthenticatePaymentRequest authenticatePaymentRequest) throws Exception;
}
