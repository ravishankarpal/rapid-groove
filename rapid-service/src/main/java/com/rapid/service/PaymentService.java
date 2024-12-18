package com.rapid.service;

import com.rapid.core.dto.payment.PaymentRequestDTO;
import com.rapid.core.dto.payment.PaymentResponse;
import com.rapid.core.entity.order.OrderDetail;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PaymentService {

    void attemptPayment();

    PaymentResponse initiatePayment(PaymentRequestDTO paymentRequestDTO);

    PaymentResponse verifyPayment(String orderId);

    boolean handleWebhook(String payload);

    ResponseEntity<String> sendPaymentRequest(Map<String, Object> paymentPayload);

   Map<String, Object> preparePaymentPayload(OrderDetail orderDetail);
}
