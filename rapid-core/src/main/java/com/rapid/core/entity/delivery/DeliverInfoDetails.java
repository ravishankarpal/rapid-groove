package com.rapid.core.entity.delivery;

import com.rapid.core.dto.Constant;
import com.rapid.core.dto.delivery.DeliveryTimeline;
import com.rapid.core.enums.DeliveryPartner;
import com.rapid.core.enums.DeliveryStatusEnum;
import com.rapid.core.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deliver_info_details")
@Getter
@Setter
@AllArgsConstructor
public class DeliverInfoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "return_window_closed_on")
    private String returnWindowClosedOn;

    @ElementCollection
    @CollectionTable(
            name = "delivery_timeline",
            joinColumns = @JoinColumn(name = "delivery_info_id")
    )
    private List<DeliveryTimeline> timelines = new ArrayList<>();

    public DeliverInfoDetails(){

        this.carrier = DeliveryPartner.DEFAULT_CARRIER.getValue();
        this.trackingNumber = DeliveryPartner.NOT_ASSIGNED.getValue();
        this.status = OrderStatus.ORDER_PLACED.getStatus();
        this.returnWindowClosedOn = getReturnWindowDate();
        DeliveryTimeline deliveryTimeline = new DeliveryTimeline();
        timelines.add(deliveryTimeline);


    }

    public static String getReturnWindowDate() {
        LocalDateTime futureDate = LocalDate.now().plusDays(15).atStartOfDay();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.RETURN_WINDOW_DATE_FORMAT);
        return futureDate.format(formatter);
    }
}
