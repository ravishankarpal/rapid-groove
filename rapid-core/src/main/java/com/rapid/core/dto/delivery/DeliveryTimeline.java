package com.rapid.core.dto.delivery;
import com.rapid.core.entity.order.OrderDetails;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DeliveryTimeline {
    private String status;
    private String timestamp;
    private String location;

}
