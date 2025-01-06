package com.rapid.dao;


import com.rapid.core.entity.checkout.CheckoutDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutDetailsRepository extends JpaRepository<CheckoutDetails, Long> {
    @Query(value = "SELECT cd.* FROM checkout_details cd JOIN checkout_items ci on cd.id=ci.checkout_details_id " +
            "WHERE ci.product_id = :productId " +
            "AND ci.size = :size AND cd.user_id = :userId", nativeQuery = true)
    CheckoutDetails findByProductIdAndSizeAndUser(Integer productId, String size, String userId);

    @Query(value = "select * from checkout_details where user_id =:userId", nativeQuery = true)
    List<CheckoutDetails> findByUser(String userId);


    @Transactional
    @Modifying
    @Query(value = "delete from checkout_details where user_id =:userId", nativeQuery = true)
    void deleteByUser(String userId);



}
