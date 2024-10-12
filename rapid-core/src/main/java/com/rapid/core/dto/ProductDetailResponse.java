package com.rapid.core.dto;


import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.ProductSizePrice;
import com.rapid.core.entity.product.Products;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class ProductDetailResponse implements Serializable {
    private Integer productId;
    private String productName;
    private String productDescription;
    private String productCategory;
    private Integer productRating;
    private Integer ratingsCount;
    private Integer reviewsCount;
    private boolean isPromotionRequired;
    private String promotions;
    private boolean isTaxIncluded;
    private List<ProductSizePriceResponseDTO> sizePrices;
    private Set<ImageModel> productImages;
    private Integer taxPercent;



}
