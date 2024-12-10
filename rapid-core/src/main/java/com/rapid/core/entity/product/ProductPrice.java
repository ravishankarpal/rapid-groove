package com.rapid.core.entity.product;


import com.rapid.core.dto.product.PriceDTO;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductPrice {


    private Double current;

    private Double original;

    private Integer discountPercentage;

    public ProductPrice(PriceDTO priceDTO){
        this.current = priceDTO.getCurrent();
        this.original = priceDTO.getOriginal();
        this.discountPercentage = priceDTO.getDiscountPercentage();
    }

    public ProductPrice(ProductPrice price) {

        this.current = price.getCurrent();
        this.original = price.getOriginal();
        this.discountPercentage = price.getDiscountPercentage();
    }
}
