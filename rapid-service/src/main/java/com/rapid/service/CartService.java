package com.rapid.service;

import com.rapid.core.dto.AddToCartRequestDTO;
import com.rapid.core.dto.CartItemResponseDTO;
import com.rapid.core.entity.order.CartItem;
import com.rapid.service.exception.RapidGrooveException;

import java.util.List;

public interface CartService {
    void addToCart(Integer productId);

    List<CartItem> getCartDetails();

    void deleteCartItem(Integer cartId);

    List<CartItemResponseDTO> getCartCartDetailsFomUserToken();

    void addItemToCart(AddToCartRequestDTO requestDTO) throws RapidGrooveException;

}
