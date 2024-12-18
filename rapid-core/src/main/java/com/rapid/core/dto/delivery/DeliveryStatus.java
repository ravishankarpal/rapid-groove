package com.rapid.core.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rapid.core.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatus {

    @JsonProperty(value = "status")
    private OrderStatus orderStatus;

    @JsonProperty(value = "reason")
    private String reason;


}
