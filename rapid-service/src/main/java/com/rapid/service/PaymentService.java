package com.rapid.service;

import com.rapid.core.dto.PaymentRequestDTO;
import com.rapid.core.dto.PaymentResponseDTO;
import com.rapid.core.dto.payment.AuthenticatePaymentRequest;

public interface PaymentService {

    PaymentResponseDTO processPayment(PaymentRequestDTO request);

    void authenticatePayment(String paymentId, AuthenticatePaymentRequest authenticatePaymentRequest) throws Exception;
}
