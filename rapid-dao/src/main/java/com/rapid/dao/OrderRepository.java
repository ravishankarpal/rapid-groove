package com.rapid.dao;

import com.rapid.core.entity.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails , Integer> {
}
