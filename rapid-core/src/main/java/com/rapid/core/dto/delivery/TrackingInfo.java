package com.rapid.core.dto.delivery;

import com.rapid.core.entity.delivery.DeliverInfoDetails;
import com.rapid.core.entity.order.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingInfo {

    private String carrier;
    private String trackingNumber;
    private String status;
    private String returnWindowClosedOn;
    private List<DeliveryTimeline> timelines;


    public TrackingInfo(DeliverInfoDetails details) {
        this.carrier = details.getCarrier();
        this.trackingNumber = details.getTrackingNumber();
        this.status = details.getStatus();
        this.timelines = details.getTimelines();
        this.returnWindowClosedOn = details.getReturnWindowClosedOn();
    }
}
