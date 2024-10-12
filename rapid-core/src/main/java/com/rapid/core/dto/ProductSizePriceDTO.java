package com.rapid.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizePriceDTO {

    private Integer sizePriceId;
    private String productName;
    private Double price;
    private String size;
    private Integer discountPercentage;
    private Integer deliveryFee;


}
