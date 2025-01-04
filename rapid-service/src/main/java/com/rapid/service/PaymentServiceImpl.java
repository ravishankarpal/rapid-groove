package com.rapid.service;


import com.rapid.core.dto.payment.AuthenticatePaymentResponse;
import com.rapid.core.dto.payment.PaymentRequest;
import com.rapid.core.dto.payment.PaymentResponseDTO;
import com.rapid.core.dto.payment.AuthenticatePaymentRequest;
import com.rapid.core.entity.User;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.entity.payment.PaymentDetails;
import com.rapid.core.enums.EndPoint;
import com.rapid.core.enums.PaymentStatusEnum;
import com.rapid.core.exception.InvalidPaymentException;
import com.rapid.core.exception.PaymentNotFoundException;
import com.rapid.core.exception.PaymentProcessingException;
import com.rapid.dao.OrderRepository;
import com.rapid.dao.PaymentDetailsRepository;
import com.rapid.dao.UserRepository;
import com.rapid.security.JwtRequestFilter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService{


    @Value("${cashfree.base.url}")
    private String cashFreeBaseUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private EmailService emailService;


    @Autowired
     private  UserRepository userRepository;

    @Override
    public PaymentResponseDTO processPayment(PaymentRequest request) {
        try {
            request.validate();
            String userName = JwtRequestFilter.CURRENT_USER;
            User user  = userRepository.findById(userName).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
            OrderDetails orderDetails = validatePaymentRequest(request);
            HttpHeaders httpHeaders  = orderService.getCashFreeHeaders();
            StringBuilder apiUrl = new StringBuilder();
            apiUrl.append(cashFreeBaseUrl).append(EndPoint.PG_ORDERS_SESSION.getEndPoint());
            HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, httpHeaders);
            ResponseEntity<PaymentResponseDTO> response = restTemplate.exchange(apiUrl.toString(), HttpMethod.POST, entity, PaymentResponseDTO.class);
            if (response.hasBody()){
                PaymentDetails payment = new PaymentDetails(response.getBody(), orderDetails, user);
                paymentDetailsRepository.saveAndFlush(payment);
                return response.getBody();
            }
            else {
                return null;
            }

        } catch (Exception e) {
            throw new PaymentProcessingException("Payment processing failed: " + e.getMessage());
        }
    }


    private OrderDetails validatePaymentRequest(PaymentRequest request) throws Exception {

        OrderDetails orderDetails = orderRepository.findByPaymentSessionId(request.getPaymentSessionId()).orElseThrow(() -> new Exception("Payment session is not found!"));

        if (orderDetails.getOrderAmount() <= 0) {
            throw new InvalidPaymentException("Invalid payment amount");
        }

        if (StringUtils.isBlank(orderDetails.getOrderMeta().getPaymentMethods())) {
            throw new InvalidPaymentException("Payment method is required");
        }

        return orderDetails;
    }

    @Override
    public AuthenticatePaymentResponse authenticatePayment(String paymentId, AuthenticatePaymentRequest authenticatePaymentRequest) throws Exception {
        PaymentDetails paymentDetails = paymentDetailsRepository.findByTransactionId(paymentId).orElseThrow(() -> new PaymentNotFoundException("Transaction Id not found!"));
        HttpHeaders httpHeaders = orderService.getCashFreeHeaders();
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(cashFreeBaseUrl).append(EndPoint.AUTHENTICATE.getEndPoint()).append(paymentId);
        HttpEntity<AuthenticatePaymentRequest> entity = new HttpEntity<>(authenticatePaymentRequest, httpHeaders);
        ResponseEntity<AuthenticatePaymentResponse> response  = restTemplate.exchange(apiUrl.toString(), HttpMethod.POST, entity, AuthenticatePaymentResponse.class);
        if (response.getStatusCode() == HttpStatus.OK){
            paymentDetails.setPaymentStatus(PaymentStatusEnum.SUCCESS.getStatus());
            paymentDetails.setPaymentDate(LocalDateTime.now());
            paymentDetails.setAuthenticateStatus("SUCCESS");
            paymentDetailsRepository.saveAndFlush(paymentDetails);
            emailService.sendOrderConfirmationEmail(paymentDetails.getOrderDetails());
            return response.getBody();
        }else{
            throw new Exception("Error while creating order with Cashfree");
        }

    }

}
