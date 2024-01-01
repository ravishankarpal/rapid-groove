package com.rapid.web.controller;


import com.rapid.core.entity.order.CartItem;
import com.rapid.service.CartService;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private CartService cartService;
   // @PreAuthorize("hasRole('User')")
    @GetMapping (value = "/addToCart/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable(name = "productId") Integer productId){
        try {
            cartService.addToCart(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ProductDetailsNotFoundException e){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/cart_details")
    public ResponseEntity<?> getCartDetails(){
       List<CartItem> cartItems =  cartService.getCartDetails();
       return new ResponseEntity<>(cartItems,HttpStatus.OK);
    }

    @DeleteMapping(value = "deleteCartItem/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartId") Integer cartId){
        cartService.deleteCartItem(cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
