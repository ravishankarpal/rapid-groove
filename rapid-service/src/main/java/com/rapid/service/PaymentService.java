package com.rapid.service;

import com.rapid.core.dto.PaymentRequestDTO;
import com.rapid.core.dto.PaymentResponseDTO;

public interface PaymentService {

    PaymentResponseDTO processPayment(PaymentRequestDTO request);
}
