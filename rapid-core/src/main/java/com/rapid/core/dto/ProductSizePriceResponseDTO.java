package com.rapid.core.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizePriceResponseDTO {
    private Integer sizePriceId;
    private Double price;
    private String size;
    private Integer discountPercentage;

    private Double discountPrice;

    private Double actualPrice;


}
