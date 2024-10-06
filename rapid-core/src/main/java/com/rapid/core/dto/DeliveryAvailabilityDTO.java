package com.rapid.core.dto;


import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryAvailabilityDTO  implements Serializable {

    private String pinCode;

    private String city;

    private String state;

    private Integer deliveryAvailable;

    private Integer estimatedDays;

}
