package com.rapid.web.controller;


import com.rapid.core.dto.payment.PaymentRequestDTO;
import com.rapid.core.dto.payment.PaymentResponse;
import com.rapid.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "rapid/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentResponse response = paymentService.initiatePayment(paymentRequestDTO);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/verify/{orderId}")
    public ResponseEntity<?> verifyPayment(@PathVariable String orderId) {
        PaymentResponse response = paymentService.verifyPayment(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
            @RequestHeader("X-Webhook-Signature") String signature) {
        boolean processed = paymentService.handleWebhook(payload);
        return processed
                ? ResponseEntity.ok("Webhook processed successfully")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid webhook");
    }

}
