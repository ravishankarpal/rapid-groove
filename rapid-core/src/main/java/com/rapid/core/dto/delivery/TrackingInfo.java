package com.rapid.core.dto.delivery;

import com.rapid.core.entity.delivery.DeliverInfoDetails;
import com.rapid.core.entity.order.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (details == null){
            this.carrier = "ASaSD";
            this.trackingNumber = "ZXMCXZC";
            this.status = "In Transit";
            DeliveryTimeline d = new DeliveryTimeline();
            d.setLocation("mnbmbb");
            d.setCurrentStatus("In transit");
            d.setLocation("mmnbn");
            this.timelines = new ArrayList<>();
            this.timelines.add(d);
            this.returnWindowClosedOn = String.valueOf(LocalDateTime.now());
            return;
        }
        this.carrier = details.getCarrier();
        this.trackingNumber = details.getTrackingNumber();
        this.status = details.getStatus();
        this.timelines = details.getTimelines();
        this.returnWindowClosedOn = details.getReturnWindowClosedOn();

    }
}
