package com.rapid.web.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rapid.core.dto.OrderDto;
import com.rapid.core.dto.payment.PaymentResponse;
import com.rapid.core.entity.order.OrderDetail;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.entity.order.OrderItem;
import com.rapid.core.enums.OrderStatus;
import com.rapid.core.exception.OrderNotFoundException;
import com.rapid.core.exception.PaymentProcessingException;
import com.rapid.core.order.OrderItemDTO;
import com.rapid.core.order.OrderRequest;
import com.rapid.core.order.OrderResponse;
import com.rapid.core.order.OrderStatusResponse;
import com.rapid.service.OrderDetailsService;
import com.rapid.service.PaymentService;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderDetailsService orderService;


    @Autowired
    private com.rapid.service.orders.OrderService service;

    @Autowired
    private PaymentService paymentService;


    @PostMapping(value = "/placeorder/{isSingleCartCheckOut}")
    public ResponseEntity<?> placeOrder(@PathVariable(name = "isSingleCartCheckOut") boolean isSingleCartCheckOut,
                                        @RequestBody OrderDto orderDto){
        try {
            orderService.placeOrder(orderDto,isSingleCartCheckOut);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        catch (ProductDetailsNotFoundException e) {
            return new ResponseEntity<>("Product details not found for product ID: "
                    + orderDto.getOrderProductQuantities().get(0).getProductId(),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/myorder/details")
    public ResponseEntity<?> getMyOrderDetails(){
        List<OrderDetails> orderDetails = orderService.getMyOrderDetails();
        return new ResponseEntity<>(orderDetails,HttpStatus.OK);
    }


//    @PostMapping(value = "/{orderId}/status")
//    public ResponseEntity<String> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
//        // Perform your logic to update order status
//
//        // Notify clients about the order update
//
//        OrderUpdate orderUpdate = new OrderUpdate();
//        orderUpdate.setOrderId(orderId);
//        orderUpdate.setOrderStatus(status);
//        orderTrackingService.sendOrderUpdate(orderUpdate);
//        return ResponseEntity.ok("Order status updated successfully");
//    }


    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) throws Exception {
        try {
            OrderDetail orderDetail = service.createOrder(orderRequest);
            Map<String, Object> paymentPayload = paymentService.preparePaymentPayload(orderDetail);
            ResponseEntity<String> paymentResponse = paymentService.sendPaymentRequest(paymentPayload);
            if (paymentResponse.getStatusCode() == HttpStatus.OK) {
                JsonNode responseBody = new ObjectMapper().readTree(paymentResponse.getBody());
                String transactionId = responseBody.path("transactionId").asText();
                orderDetail.setPaymentTransactionId(transactionId);
                orderDetail.updateOrderStatus(OrderStatus.PROCESSING);
                OrderDetail updatedOrder = service.updateOrder(orderDetail);
                return ResponseEntity.ok(OrderResponse.builder()
                        .orderId(updatedOrder.getOrderId())
                        .status(updatedOrder.getOrderStatus())
                        .paymentTransactionId(transactionId)
                        .build());
            }

            throw new PaymentProcessingException("Payment processing failed");

        } catch (Exception e) {
           // log.error("Error creating order", e);
//            throw new OrderCreationException("Failed to create order: " + e.getMessage());
            throw new Exception("Failed to create order: " + e.getMessage());
        }
    }




    @GetMapping("/status/{orderId}")
    public ResponseEntity<OrderStatusResponse> getOrderStatus(@PathVariable UUID orderId) {
        try {
            OrderDetail order = service.getOrderById(orderId);

            // Verify payment status
            PaymentResponse paymentStatus = paymentService.verifyPayment(orderId.toString());

            return ResponseEntity.ok(OrderStatusResponse.builder()
                    .orderId(order.getOrderId())
                    .orderStatus(order.getOrderStatus())
                    .paymentStatus(paymentStatus.getStatus())
                    .orderItems(mapOrderItems(order.getOrderItems()))
                    .totalAmount(order.getTotalAmount())
                    .shippingAddress(order.getShippingAddress())
                    .orderTracking(order.getOrderTracking())
                    .build());

        } catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        } catch (Exception e) {
          //  log.error("Error fetching order status", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private List<OrderItemDTO> mapOrderItems(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> OrderItemDTO.builder()
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .unitPrice(BigDecimal.valueOf(item.getUnitPrice()))
                        .totalPrice(BigDecimal.valueOf(item.getTotalPrice()))
                        .size(item.getSize())
                        .build())
                .collect(Collectors.toList());
    }



}
