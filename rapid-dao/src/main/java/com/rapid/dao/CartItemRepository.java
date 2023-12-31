package com.rapid.dao;

import com.rapid.core.entity.order.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem , Integer> {
}
