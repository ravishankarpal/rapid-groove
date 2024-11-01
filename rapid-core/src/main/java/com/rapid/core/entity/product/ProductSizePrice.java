package com.rapid.core.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "product_size_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductSizePrice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_price_id")
    private Integer sizePriceId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Double price;

    @Column(name = "size")
    private String size;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(name = "product_id")
    private Integer productId;

//    @Column(name = "is_tax_included")
//    private Integer taxIncluded;

    @Column(name = "final_price")
    private Double finalPrice;

//    @Column(name = "delivery_fee")
//    private Integer deliveryFee;




}
