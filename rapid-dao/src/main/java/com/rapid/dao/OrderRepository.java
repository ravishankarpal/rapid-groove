package com.rapid.dao;

import com.rapid.core.entity.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails , String> {

    List<OrderDetails> findByUser_Email (String userName);

}
