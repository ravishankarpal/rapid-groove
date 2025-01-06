package com.rapid.web.controller;


import com.rapid.core.dto.PaymentRequestDTO;
import com.rapid.core.dto.payment.AuthenticatePaymentResponse;
import com.rapid.core.dto.payment.PaymentRequest;
import com.rapid.core.dto.payment.PaymentResponseDTO;
import com.rapid.core.dto.payment.AuthenticatePaymentRequest;
import com.rapid.core.exception.InvalidPaymentException;
import com.rapid.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rapid/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequest request) {
            PaymentResponseDTO response = paymentService.processPayment(request);
            return ResponseEntity.ok(response);

    }


    @PostMapping(value = "/authenticate-payment/{transactionId}")
    public ResponseEntity<?> authenticatePayment(@PathVariable String transactionId,
                                                 @RequestBody AuthenticatePaymentRequest authenticatePaymentRequest) throws Exception {
        AuthenticatePaymentResponse response = paymentService.authenticatePayment(transactionId,authenticatePaymentRequest);
        return ResponseEntity.ok(response);
    }


}
