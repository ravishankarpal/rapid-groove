package com.rapid.dao;

import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.enums.OrderStatus;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails , String> {

    List<OrderDetails> findByUser_Email (String userName);

    Optional<OrderDetails> findByPaymentSessionId(String paymentSessionId);

    OrderDetails findByOrderId(String orderId);

//    @Query(value = "SELECT * FROM order_details" +
//            " WHERE user_id = :userId AND (:startDate IS NULL OR created_at >= :startDate) " +
//            "  AND (:status IS NULL OR order_status = :status)", nativeQuery = true)

//    @Query(value = "SELECT * FROM order_details WHERE user_id = :userId " +
//            "AND created_at >= COALESCE(:startDate, created_at) " +
//            "AND order_status = COALESCE(:status, order_status)",
//            nativeQuery = true)
//
//    Page<OrderDetails> searchOrders(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate,
//         @Param("status") OrderStatus status, Pageable pageable);

    @Query(value = "SELECT * FROM order_details WHERE user_id = :userId " +
            "AND created_at >= COALESCE(:startDate, created_at) " ,nativeQuery = true)

    Page<OrderDetails> searchOrders(@Param("userId") String userId, @Param("startDate") LocalDateTime startDate,Pageable pageable);



//    @Query(value = "SELECT * FROM order_details WHERE user_id = :userId", nativeQuery = true)
//    Page<OrderDetails> searchOrders(@Param("userId") String userId, Pageable pageable);





}
