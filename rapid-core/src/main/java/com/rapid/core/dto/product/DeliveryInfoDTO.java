package com.rapid.core.dto.product;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoDTO {
    private String pinCode;
    private String city;
    private String state;
    private String standardDeliveryTime;
    private Boolean expressDeliveryAvailable;
    private Double freeShippingThreshold;
    private Double shippingCost;
}
