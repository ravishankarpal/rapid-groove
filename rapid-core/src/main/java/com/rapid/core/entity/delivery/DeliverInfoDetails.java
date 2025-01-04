package com.rapid.core.entity.delivery;

import com.rapid.core.dto.delivery.DeliveryTimeline;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "deliver_info_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliverInfoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "status")
    private String status;

    @ElementCollection
    @CollectionTable(
            name = "delivery_timeline",
            joinColumns = @JoinColumn(name = "delivery_info_id")
    )
    private List<DeliveryTimeline> timelines;
}
