package com.rapid.core.entity.product;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_discount_price")
    private Double productDiscountPrice;

    @Column(name = "product_actual_price")
    private Double productActualPrice;


    @Column(name = "product_category")
    private String productCategory;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "PRODUCT_IMAGES",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            })
    private Set<ImageModel> productImages;

    @Column(name = "product_rating")
    private Integer productRating;

    @Column(name = "ratings_count")
    private Integer ratingsCount;

    @Column(name = "reviews_count")
    private Integer reviewsCount;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    @Column(name = "available_sizes")
    private String availableSizes;

    @Column(name = "is_promotion_req")
    private boolean isPromotionRequired;

    @Column(name = "promotions")
    private String promotions;

    @Column(name = "is_tax_included")
    private boolean isTaxIncluded;

    @Column(name = "size")
    private String size;





}
