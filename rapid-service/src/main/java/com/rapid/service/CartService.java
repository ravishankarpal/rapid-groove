package com.rapid.service;

import com.rapid.core.dto.*;
import com.rapid.core.dto.cart.CartRequestDTO;
import com.rapid.core.entity.cart.CartDetails;
import com.rapid.core.entity.order.CartItem;
import com.rapid.service.exception.RapidGrooveException;
import jakarta.validation.Valid;

import java.util.List;

public interface CartService {
    void addToCart(Integer productId);

    List<CartItem> getCartDetails();

    void deleteCartItem(Integer cartId);

    List<CartItemResponseDTO> getCartCartDetailsFomUserToken();

    void addItemToCart(AddToCartRequestDTO requestDTO) throws RapidGrooveException;

    void updateCartQuantity(UpdateCartDTO updateCartDTO) throws Exception;

    void processCheckout(@Valid CheckoutRequestDTO checkoutRequest) throws RapidGrooveException;


    CheckoutResponse getCheckoutDetails() throws RapidGrooveException;


    void addItemToCartV2(CartRequestDTO cartRequestDTO) throws Exception;

    CartDetails getItem() throws Exception;

    void delete(Long cartItemId) throws Exception;
}
