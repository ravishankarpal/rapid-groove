package com.rapid.dao;

import com.rapid.core.entity.order.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem , Integer> {
    @Query(value = "select ci.* from cart_item ci join cart c on ci.cart_id = c.id where c.user_id =:userName",nativeQuery = true)
    List<CartItem> getCartDetails(String userName);

    @Query(value = "select ci.* from cart_item ci join  cart c on ci.cart_id = c.id where c.user_id =:userName and ci.id =:cartItemId", nativeQuery = true)
    CartItem findCartDetailsByUserAndCartId(String userName, Integer cartItemId);
}
