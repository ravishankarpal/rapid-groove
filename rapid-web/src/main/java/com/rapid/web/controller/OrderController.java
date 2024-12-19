package com.rapid.web.controller;


import com.rapid.core.dto.OrderDto;
import com.rapid.core.dto.OrderUpdate;
import com.rapid.core.dto.orders.OrderExtend;
import com.rapid.core.dto.orders.OrderResponse;
import com.rapid.core.dto.payment.PaymentRequest;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.service.OrderService;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;



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
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest paymentRequest) throws Exception {

       OrderResponse orderResponse = orderService.createOrder(paymentRequest);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }


    @GetMapping("/details/{order_id}")
    public ResponseEntity<?> getOrder(@PathVariable("order_id") String orderId) throws Exception {
        OrderResponse orderResponse = orderService.getOrder(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }


    @PatchMapping("/terminate/{order_id}")
    public ResponseEntity<?> terminateOrder(@PathVariable("order_id") String orderId) throws Exception {
        OrderResponse orderResponse = orderService.terminateOrder(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }



    @PatchMapping("/extend/{order_id}")
    public ResponseEntity<?> getOrderExtend(@PathVariable("order_id") String orderId) throws Exception {
        OrderResponse orderResponse = orderService.getOrderExtend(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }


    @PatchMapping("update/extend/{order_id}")
    public ResponseEntity<?> getOrderExtend(@PathVariable("order_id") String orderId,
                                            @RequestBody OrderExtend orderExtend) throws Exception {
        OrderResponse orderResponse = orderService.updateOrderExtend(orderId, orderExtend);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }



}
