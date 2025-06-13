package com.rapid.web.controller;


import com.rapid.core.dto.*;
import com.rapid.core.dto.cart.CartRequestDTO;
import com.rapid.core.dto.checkout.CheckoutDTO;
import com.rapid.core.dto.checkout.CheckoutRequest;
import com.rapid.core.dto.checkout.CheckoutRequestResponse;
import com.rapid.core.entity.cart.CartDetails;
import com.rapid.core.entity.order.CartItem;
import com.rapid.service.CartService;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import com.rapid.service.exception.RapidGrooveException;
import com.rapid.service.exception.TokenExpiredException;
import jakarta.validation.Valid;
import org.apache.el.parser.TokenMgrError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "rapid/cart")
public class CartController {

    @Autowired
    private CartService cartService;
   // @PreAuthorize("hasRole('User')")



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


    @PutMapping(value = "/update-quantity")
    public ResponseEntity<?> updateCartQuantity(@RequestBody UpdateCartDTO updateCartDTO) throws Exception {
        cartService.updateCartQuantity(updateCartDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/checkout")
    public ResponseEntity<?> processCheckout(@Valid @RequestBody CheckoutRequestDTO checkoutRequest) throws RapidGrooveException {
         cartService.processCheckout(checkoutRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/checkoutdetails")
    public ResponseEntity<?> getCheckoutDetails() throws RapidGrooveException {
       CheckoutResponse checkoutResponse =  cartService.getCheckoutDetails();
        return new ResponseEntity<>(checkoutResponse,HttpStatus.OK);
    }

    @PostMapping  (value = "/v2/addItemToCart")
    public ResponseEntity<?> addItemToCartV2(@RequestBody CartRequestDTO cartRequestDTO) throws Exception {
        Integer qty = cartService.addItemToCartV2(cartRequestDTO);
        return new ResponseEntity<>(qty,HttpStatus.OK);

    }

    @GetMapping(value = "/get")
    public ResponseEntity<?> getItem() throws TokenExpiredException,Exception {
        CartDetails cartDetails = cartService.getItem();
        return new ResponseEntity<>(cartDetails,HttpStatus.OK);

    }


    @DeleteMapping(value = "delete/{cartItemId}")
    public ResponseEntity<?> deleteCartDetailsItem(@PathVariable("cartItemId") Long cartId) throws Exception {
        cartService.delete(cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/save/checkout")
    public ResponseEntity<?> saveCheckoutDetails(@RequestBody CheckoutRequest checkoutRequest){
        CheckoutRequestResponse checkoutRequestResponse = cartService.saveCheckoutDetails(checkoutRequest);
        return ResponseEntity.ok(checkoutRequestResponse);
    }

    @PostMapping("/checkout/details")
    public ResponseEntity<?> getCheckoutDetails(@RequestBody CheckoutDTO checkoutDTO) throws Exception {
       com.rapid.core.dto.checkout.CheckoutResponse checkoutItemResponse =  cartService.getCheckoutDetails(checkoutDTO);
        return ResponseEntity.ok(checkoutItemResponse);
    }




}
