package com.rapid.dao;

import com.rapid.core.entity.User;
import com.rapid.core.entity.cart.CartDetails;
import com.rapid.core.entity.cart.CartItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetails, Long> {


    CartDetails findByUser(User user);

    @Query(value = "select cd.id, cid.id from cart_details cd join cart_item_details cid " +
            "where cd.user_id =:userId and cid.id =:CartItemId", nativeQuery = true)

    List<Object[]>  findCartDetailsByUserAndCartItemId(@Param("userId") String userId, @Param("CartItemId") Long CartItemId);

    Optional<CartDetails> findByCartItemDetailsContaining(CartItemDetails cartItemDetails);


}
