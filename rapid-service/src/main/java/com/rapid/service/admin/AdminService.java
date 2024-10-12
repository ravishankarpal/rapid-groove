package com.rapid.service.admin;

import com.rapid.core.dto.DeliveryAvailabilityDTO;
import com.rapid.core.entity.DeliveryAvailability;
import com.rapid.service.exception.RapidGrooveException;

public interface AdminService {
    DeliveryAvailability updateDeliveryAvailability(DeliveryAvailabilityDTO deliveryAvailability) throws RapidGrooveException;
}
