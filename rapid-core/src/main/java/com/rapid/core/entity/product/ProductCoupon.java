package com.rapid.core.entity.product;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "product_coupon")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCoupon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String couponName;

    @Column(nullable = false)
    private Double discountPercentage;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    // Optional description for the coupon
    private String description;

    // Link back to ProductDetails
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductDetails productDetails;


}
