package com.rapid.core.entity.product;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.rapid.core.dto.ProductDetailDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId ;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;


    // need to remove
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


    @Column(name = "is_promotion_req")
    private Integer isPromotionRequired;

    @Column(name = "promotions")
    private String promotions;




    public Products(ProductDetailDTO productDetailDTO){
        this.productId = productDetailDTO.getProductId();
        this.productName = productDetailDTO.getProductName();
        this.productDescription = productDetailDTO.getProductDescription();
        this.productCategory = productDetailDTO.getProductCategory();
        this.productRating = productDetailDTO.getProductRating();
        this.ratingsCount = productDetailDTO.getRatingsCount();
        this.reviewsCount = productDetailDTO.getReviewsCount();
        this.isPromotionRequired = productDetailDTO.isPromotionRequired() ? 1 :0;
        if(this.isPromotionRequired == 1) {
            this.promotions = productDetailDTO.getPromotions();
        }



    }





}
