package com.rapid.dao;

import com.rapid.core.entity.order.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart , Integer> {

    @Query(value = "select * from cart where user_id =:userName",nativeQuery = true)
    List<Cart> findCartByUserId(String userName);
}
