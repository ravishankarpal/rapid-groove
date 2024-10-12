package com.rapid.dao;

import com.rapid.core.entity.order.Cart;
import com.rapid.core.entity.order.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart , Integer> {

    @Query(value = "select * from cart where user_id =:userName",nativeQuery = true)
    List<Cart> findCartByUserId(String userName);


    //void deleteByUser_UserName (String userName);

    @Query(value = "select * from cart where user_id =:userName",nativeQuery = true)
    Cart findCartByUserName(String userName);

    void deleteByUser_Email (String userName);

}
