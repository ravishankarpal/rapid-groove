package com.rapid.core.dto.delivery;
import com.rapid.core.entity.order.OrderDetails;
import com.rapid.core.enums.DeliveryPartner;
import com.rapid.core.enums.OrderStatus;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@AllArgsConstructor
@Embeddable
public class DeliveryTimeline {
    private String currentStatus;
    private String timestamp;
    private String location;

    public DeliveryTimeline(){
        this.currentStatus = OrderStatus.ORDER_PLACED.getStatus();
        this.timestamp = LocalDateTime.now().toString();
        this.location = DeliveryPartner.WAREHOUSE.getValue();
    }

}
