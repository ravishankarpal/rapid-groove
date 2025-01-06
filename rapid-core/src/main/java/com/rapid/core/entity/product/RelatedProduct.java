package com.rapid.core.entity.product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


@Data
@Getter
@Setter
public class RelatedProduct implements Serializable {


    private String name;

    private String subtitle;

    private String description;

    private ProductSize productSize;

    private ProductRating rating;

    private Set<ImageModel> productImages;



    public RelatedProduct(ProductDetails relatedProductDetails, Set<ImageModel> imageModels) {
        this.name = relatedProductDetails.getName();
        this.subtitle = relatedProductDetails.getSubtitle();
        this.description = relatedProductDetails.getDescription();
        this.productSize = relatedProductDetails.getSizes().get(0);
        //this.price = new ProductPrice(relatedProductDetails.getPrice());
        this.rating = new ProductRating(relatedProductDetails.getRating());
        this.productImages =imageModels;


    }
}
