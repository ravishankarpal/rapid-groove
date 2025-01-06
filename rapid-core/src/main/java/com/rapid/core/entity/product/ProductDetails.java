package com.rapid.core.entity.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.rapid.core.dto.product.ProductDTO;
import com.rapid.core.dto.product.ReviewDTO;
import com.rapid.core.dto.product.SizeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "product_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProductDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String subtitle;

    @Column(length = 1000)
    private String description;


    @Column(length = 1000)
    private String category;

    @Column(length = 1000)
    private String subCategory;

//    @Embedded
//    private ProductPrice price;

    @Embedded
    private ProductRating rating;


    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "PRODUCT_IMAGES",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            })

    private Set<ImageModel> productImages;

//    @JoinColumn(name = "product_id")
//    private Set<ImageModel> productImages;




//    @CollectionTable(name = "product_images_details", joinColumns = @JoinColumn(name = "product_id"))
//    private List<String> images;

    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    private List<ProductSize> sizes;

    @ElementCollection
    @CollectionTable(name = "product_specifications", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> specifications;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductReview> reviews;

   @Embedded
    private DeliveryInfo deliveryInfo;

   @Transient
   @JsonInclude
   @JsonProperty("relatedProducts")
   private List<RelatedProduct> relatedProducts;


    public ProductDetails(ProductDTO productDTO, Set<ImageModel> imageModels) {
        this.name = productDTO.getName();
        this.subtitle = productDTO.getSubtitle();
        this.description = productDTO.getDescription();
       // this.price = new ProductPrice(productDTO.getPrice());
        this.rating = new ProductRating(productDTO.getRating());
        this.category = productDTO.getCategory();
        this.productImages = imageModels;
        List<ProductSize> productSizes = new ArrayList<>();
        for (SizeDTO sizeDTO: productDTO.getSizes()){
            ProductSize productSize = new ProductSize(sizeDTO);
            productSizes.add(productSize);
        }
        this.sizes = productSizes;
        this.specifications = productDTO.getSpecifications();
        List<ProductReview> productReviews = new ArrayList<>();
        for (ReviewDTO reviewDTO : productDTO.getReviews()){
            ProductReview productReview = new ProductReview(reviewDTO);
            productReviews.add(productReview);

        }
        this.reviews = productReviews;
        this.deliveryInfo =new DeliveryInfo(productDTO.getDeliveryInfo());

    }
}
