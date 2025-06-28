package com.rapid.service;

import com.rapid.core.dto.*;
import com.rapid.core.dto.cart.CartRequestDTO;
import com.rapid.core.dto.checkout.CheckoutDTO;
import com.rapid.core.dto.checkout.CheckoutRequest;
import com.rapid.core.dto.checkout.CheckoutRequestResponse;
import com.rapid.core.entity.cart.CartDetails;
import com.rapid.core.entity.order.CartItem;
import com.rapid.service.exception.RapidGrooveException;
import com.rapid.service.exception.TokenExpiredException;
import jakarta.validation.Valid;

import java.util.List;

public interface CartService {

    List<CartItem> getCartDetails();

    void deleteCartItem(Integer cartId);

    void updateCartQuantity(UpdateCartDTO updateCartDTO) throws Exception;

    void processCheckout(@Valid CheckoutRequestDTO checkoutRequest) throws RapidGrooveException;


    CheckoutResponse getCheckoutDetails() throws RapidGrooveException;


    Integer addItemToCartV2(CartRequestDTO cartRequestDTO) throws Exception;

    CartDetails getItem() throws TokenExpiredException, Exception;

    void delete(Long cartItemId) throws Exception;

    CheckoutRequestResponse saveCheckoutDetails(CheckoutRequest checkoutRequest);

    com.rapid.core.dto.checkout.CheckoutResponse getCheckoutDetails(CheckoutDTO checkoutDTO) throws Exception;
}
