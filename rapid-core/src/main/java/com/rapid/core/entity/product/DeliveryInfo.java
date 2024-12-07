package com.rapid.core.entity.product;


import com.rapid.core.dto.product.DeliveryInfoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor

public class DeliveryInfo {

    private String pinCode;

    private String city;

    private String state;

    private String standardDeliveryTime;

    private Boolean expressDeliveryAvailable;

    private Double freeShippingThreshold;

    private Double shippingCost;

    public DeliveryInfo(DeliveryInfoDTO deliveryInfo) {
        this.pinCode = deliveryInfo.getPinCode();
        this.city = deliveryInfo.getCity();
        this.state = deliveryInfo.getState();
        this.standardDeliveryTime = deliveryInfo.getStandardDeliveryTime();
        this.expressDeliveryAvailable = deliveryInfo.getExpressDeliveryAvailable();
        this.freeShippingThreshold = deliveryInfo.getFreeShippingThreshold();
        this.shippingCost = deliveryInfo.getShippingCost();

    }
}
