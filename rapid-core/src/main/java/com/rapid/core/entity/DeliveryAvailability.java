package com.rapid.core.entity;

import com.rapid.core.dto.DeliveryAvailabilityDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "delivery_availability")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DeliveryAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id" )
    private Integer id;

    @Column(name = "pin_code", unique = true, nullable = false, length = 10)
    private String pinCode;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "delivery_available", nullable = false,columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer deliveryAvailable ;

    @Column(name = "estimated_days")
    private Integer estimatedDays;


    @Override
    public String toString() {
        return "DeliveryAvailability{" +
                "id=" + id +
                ", pincode='" + pinCode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", deliveryAvailable=" + deliveryAvailable +
                ", estimatedDays=" + estimatedDays +
                '}';
    }


    public  DeliveryAvailability(DeliveryAvailabilityDTO deliveryAvailabilityDTO){
        this.pinCode = deliveryAvailabilityDTO.getPinCode();
        this.city = deliveryAvailabilityDTO.getCity();
        this.state = deliveryAvailabilityDTO.getState();
        this.deliveryAvailable = deliveryAvailabilityDTO.getDeliveryAvailable();
        this.estimatedDays = deliveryAvailabilityDTO.getEstimatedDays();
    }
}
