package com.rapid.web.controller;


import com.rapid.core.dto.OrderDto;
import com.rapid.core.dto.orders.OrderExtend;
import com.rapid.core.dto.orders.CashFreeOrderResponse;
import com.rapid.core.dto.orders.OrderRequest;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.entity.delivery.DeliverInfoDetails;
import com.rapid.core.enums.OrderStatus;
import com.rapid.service.exception.TokenExpiredException;
import com.rapid.service.EmailService;
import com.rapid.service.OrderService;
import com.rapid.service.PdfService;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @Autowired
    private EmailService emailService;


    @Autowired
    private PdfService pdfService;



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

//    @GetMapping(value = "/myorder/details")
//    public ResponseEntity<?> getMyOrderDetails(){
//        List<OrderDetails> orderDetails = orderService.getMyOrderDetails();
//        return new ResponseEntity<>(orderDetails,HttpStatus.OK);
//    }


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

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest paymentRequest) throws Exception {

       CashFreeOrderResponse orderResponse = orderService.createOrder(paymentRequest);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }


//    @GetMapping("/details/{order_id}")
//    public ResponseEntity<?> getOrder(@PathVariable("order_id") String orderId) throws Exception {
//        CashFreeOrderResponse orderResponse = orderService.getOrder(orderId);
//        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
//    }


    @PatchMapping("/terminate/{order_id}")
    public ResponseEntity<?> terminateOrder(@PathVariable("order_id") String orderId) throws Exception {
        CashFreeOrderResponse orderResponse = orderService.terminateOrder(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }



    @PatchMapping("/extend/{order_id}")
    public ResponseEntity<?> getOrderExtend(@PathVariable("order_id") String orderId) throws Exception {
        CashFreeOrderResponse orderResponse = orderService.getOrderExtend(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }


    @PatchMapping("update/extend/{order_id}")
    public ResponseEntity<?> getOrderExtend(@PathVariable("order_id") String orderId,
                                            @RequestBody OrderExtend orderExtend) throws Exception {
        CashFreeOrderResponse orderResponse = orderService.updateOrderExtend(orderId, orderExtend);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

    @PostMapping("/test/email/{order_id}")
    public ResponseEntity<?> sendOrderConfirmationEmail(@PathVariable("order_id") String orderId) throws MessagingException, IOException {
        orderService.sendOrderConfirmationEmail(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/v2/details")
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @RequestParam(required = false) String period,
            @RequestParam(defaultValue = "ACTIVE", required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        Page<OrderResponse> orderResponses =  orderService.getOrders( period, orderStatus, page, size);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getOrderDetailsById(@PathVariable("id") String id) throws TokenExpiredException {

        OrderResponse response = orderService.getOrderDetailsById(id);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/invoice/{order_id}")
    public ResponseEntity<?> generateInvoice(@PathVariable("order_id") String orderId) throws FileNotFoundException {
        String userHome = System.getProperty("user.home");
        String pdfPath = userHome + File.separator + "downloads" + File.separator + "invoice_" + orderId + ".pdf";

        pdfService.generatePDF(orderId,pdfPath);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/track/order/{order_id}")
    public ResponseEntity<?> trackOrder(@PathVariable("order_id") String orderId){
        DeliverInfoDetails details = orderService.trackOrder(orderId);
        return ResponseEntity.ok(details);
    }




}

