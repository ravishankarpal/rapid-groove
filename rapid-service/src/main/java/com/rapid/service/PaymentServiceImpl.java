package com.rapid.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapid.core.dto.payment.PaymentRequestDTO;

import com.rapid.core.dto.payment.PaymentResponse;

import com.rapid.core.entity.order.OrderDetail;
import com.rapid.core.enums.PaymentStatusEnum;

import com.rapid.dao.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${cashfree.clientId}")
    private String clientId;

    @Value("${cashfree.clientSecret}")
    private String clientSecret;

    @Value("${cashfree.baseUrl}")
    private String baseUrl;

    @Value("${cashfree.webhookSecret}")
    private String webhookSecret;

    @Value("${cashfree.paymentUrl}")
    private String paymentUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderDetailsService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private void validatePaymentRequest(PaymentRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }

        if (StringUtils.isEmpty(request.getOrderId())) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(0.0) <= 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }
    }


    @Override
    public void attemptPayment() {
        PaymentRequestDTO paymentRequestDTO = null;
        PaymentResponse paymentResponse=  initiatePayment(paymentRequestDTO);
        PaymentResponse response =  verifyPayment(paymentRequestDTO.getOrderId());
        String payload = null;
        boolean webhook  = handleWebhook(payload);
    }

    @Override
    public PaymentResponse initiatePayment(PaymentRequestDTO request) {
        try {
            validatePaymentRequest(request);
            Map<String, Object> payload = prepareCashfreePayload(request);
            ResponseEntity<String> response = sendPaymentRequest(payload);
            return processPaymentInitiationResponse(response, request);
        } catch (Exception e) {
            log.error("Payment initiation failed", e);
            return PaymentResponse.builder()
                    .orderId(request.getOrderId())
                    .status(PaymentStatusEnum.FAILED)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    private Map<String, Object> prepareCashfreePayload(PaymentRequestDTO request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("appId", clientId);
        payload.put("secretKey", clientSecret);
        payload.put("orderId", request.getOrderId());
        payload.put("orderAmount", request.getAmount());
        payload.put("orderCurrency", "INR");
        Map<String, String> customer = new HashMap<>();
        customer.put("customerId", request.getEmail());
        customer.put("customerName", request.getName());
        customer.put("customerEmail", request.getEmail());
        customer.put("customerPhone", request.getPhone());
        payload.put("customer", customer);
        payload.put("environment", "test");
        return payload;
    }


    private PaymentResponse processPaymentInitiationResponse(
            ResponseEntity<String> response,
            PaymentRequestDTO request) {
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode responseBody = objectMapper.readTree(response.getBody());
                return PaymentResponse.builder()
                        .orderId(request.getOrderId())
                        .paymentSessionId(responseBody.path("paymentSessionId").asText())
                        .paymentLink(responseBody.path("paymentLink").asText())
                        .status(PaymentStatusEnum.INITIATED)
                        .build();
            } catch (Exception e) {
                log.error("Failed to parse payment response", e);
                return PaymentResponse.builder()
                        .orderId(request.getOrderId())
                        .status(PaymentStatusEnum.FAILED)
                        .errorMessage("Failed to process payment response")
                        .build();
            }
        } else {
            return PaymentResponse.builder()
                    .orderId(request.getOrderId())
                    .status(PaymentStatusEnum.FAILED)
                    .errorMessage("Payment request failed: " + response.getStatusCode())
                    .build();
        }
    }


   @Override
    public PaymentResponse verifyPayment(String orderId) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("appId", clientId);
            payload.put("secretKey", clientSecret);
            payload.put("orderId", orderId);

            // Make verification request
            ResponseEntity<String> response = restTemplate.postForEntity(
                    paymentUrl + "/order/status", payload,String.class  );
            // Process verification response
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = objectMapper.readTree(response.getBody());
                String status = responseBody.path("orderStatus").asText();
                return PaymentResponse.builder()
                        .orderId(orderId)
                        .status(mapPaymentStatus(status))
                        .build();
            }

            return PaymentResponse.builder()
                    .orderId(orderId)
                    .status(PaymentStatusEnum.FAILED)
                    .build();
        } catch (Exception e) {
            log.error("Payment verification failed", e);
            return PaymentResponse.builder()
                    .orderId(orderId)
                    .status(PaymentStatusEnum.FAILED)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    private PaymentStatusEnum mapPaymentStatus(String cashfreeStatus) {
        switch (cashfreeStatus.toUpperCase()) {
            case "PAID":
                return PaymentStatusEnum.SUCCESS;
            case "ACTIVE":
                return PaymentStatusEnum.INITIATED;
            case "CANCELLED":
                return PaymentStatusEnum.CANCELLED;
            default:
                return PaymentStatusEnum.FAILED;
        }
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(clientId, clientSecret);
        return headers;
    }


    public ResponseEntity<String> sendPaymentRequest(Map<String, Object> payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        return restTemplate.postForEntity(
                paymentUrl + "/order/create",   request, String.class
        );
    }

    @Override
    public boolean handleWebhook(String payload) {
        try {
            if (!validateWebhookSignature(payload)) {
                log.warn("Invalid webhook signature");
                return false;
            }
            JsonNode webhookData = objectMapper.readTree(payload);
            String orderId = webhookData.path("orderId").asText();
            String status = webhookData.path("orderStatus").asText();
            log.info("Webhook received for order: {}, status: {}", orderId, status);
            return true;
        } catch (Exception e) {
            log.error("Webhook processing failed", e);
            return false;
        }
    }

    private boolean validateWebhookSignature(String payload) {
        // Implement webhook signature validation
        // This would involve generating HMAC signature and comparing
        return true; // Placeholder
    }

   @Override
    public Map<String, Object> preparePaymentPayload(OrderDetail orderDetail) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", orderDetail.getOrderId().toString());
        payload.put("amount", orderDetail.getTotalAmount());
        payload.put("currency", "INR");
        payload.put("customerEmail", orderDetail.getUser().getEmail());
        return payload;
    }

}
