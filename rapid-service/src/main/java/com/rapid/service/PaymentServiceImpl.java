package com.rapid.service;


import com.rapid.core.dto.PaymentRequestDTO;
import com.rapid.core.dto.PaymentResponseDTO;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.dto.payment.AuthenticatePaymentRequest;
import com.rapid.core.dto.payment.PaymentRequest;
import com.rapid.core.entity.Payment;
import com.rapid.core.enums.EndPoint;
import com.rapid.core.enums.PaymentStatusEnum;
import com.rapid.core.exception.InvalidPaymentException;
import com.rapid.core.exception.PaymentProcessingException;
import com.rapid.dao.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;


    @Value("${cashfree.base.url}")
    private String cashFreeBaseUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;


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



    @Override
    public void authenticatePayment(String paymentId, AuthenticatePaymentRequest authenticatePaymentRequest) throws Exception {

        HttpHeaders httpHeaders = orderService.getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.SUBMIT_OTP.getEndPoint()).append(paymentId);

        HttpEntity<AuthenticatePaymentRequest> entity = new HttpEntity<>(authenticatePaymentRequest, httpHeaders);
        ResponseEntity<String> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.POST, entity, String.class);
//        if (response.getStatusCode() == HttpStatus.OK){
//            return response.getBody();
//        }else{
//            throw new Exception("Error while creating order with Cashfree");
//        }

    }

}
