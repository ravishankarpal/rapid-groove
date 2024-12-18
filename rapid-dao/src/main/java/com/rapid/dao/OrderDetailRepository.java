package com.rapid.dao;

import com.rapid.core.entity.order.OrderDetail;
import com.rapid.core.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository

public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID>{

    List<OrderDetail> findByUserEmailAndOrderStatus(String email, OrderStatus status);
    List<OrderDetail> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}
