package com.rapid.dao;

import com.rapid.core.entity.CheckoutRequest;
import com.rapid.core.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRequestRepository  extends JpaRepository<CheckoutRequest, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM checkout_items ci WHERE ci.checkout_request_id IN (SELECT cr.id FROM checkout_requests cr WHERE cr.user_id = :userId); " +
            "DELETE FROM checkout_requests WHERE user_id = :userId",
            nativeQuery = true)
    void deleteCheckoutRequestAndItemsByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM checkout_items WHERE checkout_request_id IN " +
            "(SELECT id FROM checkout_requests WHERE user_id = :userId)",
            nativeQuery = true)
    void deleteCheckoutItemsByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM checkout_requests WHERE user_id = :userId",
            nativeQuery = true)
    void deleteCheckoutRequestsByUserId(@Param("userId") String userId);

    @Query(value = "select ci.product_id,ci.product_name,ci.quantity, ci.size,ci.price,ci.pic_byte , cr.total_amount,discount_amount from " +
            " checkout_requests cr join checkout_items ci on cr.id = ci.checkout_request_id" +
            " where cr.user_id =:userId",nativeQuery = true)
    Object[] findByUserId(@Param("userId") String userId);
}
