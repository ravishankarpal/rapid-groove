package com.rapid.web.controller;


import com.rapid.core.dto.OrderDto;
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

    @GetMapping(value = "/myorder/details")
    public ResponseEntity<?> getMyOrderDetails(){
        List<OrderDetails> orderDetails = orderService.getMyOrderDetails();
        return new ResponseEntity<>(orderDetails,HttpStatus.OK);
    }


}
