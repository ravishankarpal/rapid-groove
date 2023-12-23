package com.rapid.web.controller;


import com.rapid.core.dto.OrderDto;
import com.rapid.service.OrderService;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/placeorder")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDto orderDto){
        try {
            orderService.placeOrder(orderDto);
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


}
