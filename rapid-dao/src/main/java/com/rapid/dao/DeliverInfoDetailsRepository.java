package com.rapid.dao;

import com.rapid.core.entity.delivery.DeliverInfoDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliverInfoDetailsRepository extends JpaRepository<DeliverInfoDetails, Long> {

    DeliverInfoDetails findByOrderId(String orderId);
}
