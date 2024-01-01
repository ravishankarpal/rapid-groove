package com.rapid.service;

import com.rapid.core.entity.order.CartItem;

import java.util.List;

public interface CartService {
    void addToCart(Integer productId);

    List<CartItem> getCartDetails();

    void deleteCartItem(Integer cartId);
}
