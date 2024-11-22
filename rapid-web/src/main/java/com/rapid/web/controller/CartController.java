package com.rapid.web.controller;


import com.rapid.core.dto.*;
import com.rapid.core.entity.order.CartItem;
import com.rapid.service.CartService;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.validation.Valid;
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

    @PostMapping  (value = "/addToCart/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable(name = "productId") Integer productId){
        try {
            cartService.addToCart(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ProductDetailsNotFoundException e){
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/details")
    public ResponseEntity<?> getCartCartDetailsFomUserToken(){
        List<CartItemResponseDTO> cartItemResponseDTO =  cartService.getCartCartDetailsFomUserToken();
        return new ResponseEntity<>(cartItemResponseDTO,HttpStatus.OK);
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


    @PostMapping  (value = "/addItemToCart")
    public ResponseEntity<?> addItemToCart(@RequestBody AddToCartRequestDTO cartRequestDTO) throws RapidGrooveException {
            cartService.addItemToCart(cartRequestDTO);
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





}
