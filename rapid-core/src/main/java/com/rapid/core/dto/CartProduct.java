package com.rapid.core.dto;

import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.ProductImages;
import com.rapid.core.entity.product.Products;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
public class CartProduct {

    private Integer productId;
    private String productName;
    private String productDescription;
    private String productCategory;
    private Integer taxPercent;
    private Set<ImageModel> productImages;
    private String promotions;

    public CartProduct(Products products){
        this.productId = products.getProductId();
        this.productName = products.getProductName();
        this.productDescription = products.getProductDescription();
        this.productCategory = products.getProductCategory();
        this.productImages = products.getProductImages();
        if (BooleanUtils.toBoolean(products.getIsPromotionRequired())){
            this.promotions = products.getPromotions();
        }


    }
}
