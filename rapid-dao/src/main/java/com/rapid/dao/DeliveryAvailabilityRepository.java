package com.rapid.dao;

import com.rapid.core.entity.DeliveryAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAvailabilityRepository extends JpaRepository<DeliveryAvailability,Integer> {

    DeliveryAvailability findByPinCode(String pinCode);
}
