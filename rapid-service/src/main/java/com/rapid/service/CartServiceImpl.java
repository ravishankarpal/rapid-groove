package com.rapid.service;


import com.rapid.core.entity.User;
import com.rapid.core.entity.order.Cart;
import com.rapid.core.entity.order.CartItem;
import com.rapid.core.entity.product.Products;
import com.rapid.dao.CartItemRepository;
import com.rapid.dao.CartRepository;
import com.rapid.dao.ProductRepository;
import com.rapid.dao.UserRepository;
import com.rapid.security.JwtRequestFilter;
import com.rapid.security.JwtTokenDetails;
import com.rapid.service.exception.ProductDetailsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CartServiceImpl implements CartService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private CartRepository cartRepository ;

    @Autowired
    private CartItemRepository cartItemRepository ;

    @Autowired
    private JwtTokenDetails jwtTokenDetails;
    @Override
    public void addToCart(Integer productId) {
        log.info("going to add product in cart for productId :{}",productId);
        Optional<Products> products = productRepository.findById(productId);
        String userName  = null;
        if (products.isPresent()){
            userName =  JwtRequestFilter.CURRENT_USER;
            Optional<User> user = userRepository.findById(userName);
            if (user.isPresent()){
                log.info("going to add product in cart for productId :{} by user :{}"
                        ,productId,user.get().getUserName());
                List<Cart> cart = cartRepository.findCartByUserId(user.get().getUserName());
                if(CollectionUtils.isEmpty(cart)){
                    cart.add(new Cart(user.get()));
                }
                List<CartItem> existingItem = cart.get(0).getCartItems().stream()
                        .filter(item -> item.getProducts().equals(products.get()))
                        .toList();

                if (!CollectionUtils.isEmpty(existingItem)) {
                    existingItem.get(0).setQuantity(existingItem.get(0).getQuantity() + 1);
                } else {
                    CartItem cartItem = new CartItem();
                    cartItem.setProducts(products.get());
                    cartItem.setQuantity(1);
                    cartItem.setCart(cart.get(0));
                    cart.get(0).getCartItems().add(cartItem);
                }
                cartRepository.saveAndFlush(cart.get(0));
                log.info("Product has been successfully added in user cart username :{} cartDetails :{}",
                        userName,cart);
            }
        }
        else{
            log.info("Product Details not found!");
            throw  new ProductDetailsNotFoundException("Product Details not found!");
        }
    }

    @Override
    public List<CartItem> getCartDetails() {
        String userName = JwtRequestFilter.CURRENT_USER;
        List<CartItem> cartItems =  cartItemRepository.getCartDetails(userName);
        return  cartItems;
    }

    @Override
    public void deleteCartItem(Integer cartId) {
        cartRepository.deleteById(cartId);
        cartItemRepository.deleteById(cartId);
    }
}
