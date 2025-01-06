package com.rapid.core.dto;

import com.rapid.core.entity.product.ImageModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;

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


}
