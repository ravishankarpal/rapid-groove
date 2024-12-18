package com.rapid.core.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rapid.core.dto.delivery.DeliveryStatus;
import com.rapid.core.dto.delivery.ShipmentDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderExtend {

    @JsonProperty(value = "order_delivery_status")
    private DeliveryStatus deliveryStatus;

    @JsonProperty(value = "shipment_details")
    private ShipmentDetails shipmentDetails;

}
