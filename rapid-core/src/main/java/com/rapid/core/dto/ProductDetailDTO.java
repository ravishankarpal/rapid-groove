package com.rapid.core.dto;



import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductDetailDTO {

    private Integer productId;
    private String productName;
    private String productDescription;
    private String productCategory;
    private Integer productRating;
    private Integer ratingsCount;
    private Integer reviewsCount;
    private boolean promotionRequired;
    private String promotions;
    private boolean taxIncluded;
    private Integer taxPercent;
    private List<ProductSizePriceDTO> sizePrices;

}
