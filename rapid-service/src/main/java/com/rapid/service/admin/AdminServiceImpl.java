package com.rapid.service.admin;

import com.rapid.core.dto.DeliveryAvailabilityDTO;
import com.rapid.core.entity.DeliveryAvailability;
import com.rapid.dao.DeliveryAvailabilityRepository;
import com.rapid.service.exception.RapidGrooveException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AdviceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DeliveryAvailabilityRepository deliveryAvailabilityRepository;


    @Override
    public DeliveryAvailability updateDeliveryAvailability(DeliveryAvailabilityDTO deliveryAvailability) throws RapidGrooveException {

        if (deliveryAvailability == null){
            throw new RapidGrooveException("Delivery update field should not be blank");
        }
        DeliveryAvailability delivery = new DeliveryAvailability(deliveryAvailability);
        deliveryAvailabilityRepository.saveAndFlush(delivery);
        return delivery;

    }
}
