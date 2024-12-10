package com.rapid.dao;


import com.rapid.core.entity.cart.CartItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsDetailsRepository extends JpaRepository<CartItemDetails, Long> {


    @Query(value = " select cid.* from cart_details cd join cart_item_details cid " +
            "where cd.user_id =:userId and cid.id =:cartItemId", nativeQuery = true)

    Optional<CartItemDetails> findByUserIdAndCartItem(@Param("userId") String userId, Long cartItemId);
}
