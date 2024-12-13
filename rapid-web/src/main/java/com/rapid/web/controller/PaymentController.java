package com.rapid.web.controller;


import com.rapid.core.dto.PaymentRequestDTO;
import com.rapid.core.dto.PaymentResponseDTO;
import com.rapid.core.dto.payment.AuthenticatePaymentRequest;
import com.rapid.core.exception.InvalidPaymentException;
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

    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO request) {
        try {
            PaymentResponseDTO response = paymentService.processPayment(request);
            return ResponseEntity.ok(response);
        } catch (InvalidPaymentException e) {
            return ResponseEntity.badRequest()
                    .body(new PaymentResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponseDTO(false, "Payment processing failed: " + e.getMessage()));
        }
    }


    @PostMapping(value = "/authenticate-payment/{orderId}")
    public ResponseEntity<?> authenticatePayment(@PathVariable("cf_payment_id") String paymentId,
                                                 @RequestBody AuthenticatePaymentRequest authenticatePaymentRequest) throws Exception {
        paymentService.authenticatePayment(paymentId,authenticatePaymentRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
