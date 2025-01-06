package com.rapid.core.dto.product;

import com.rapid.core.entity.product.ImageModel;
import lombok.Data;

import java.util.List;
import java.util.Set;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String name;
    private String subtitle;
    private String description;
   // private PriceDTO price;
    private RatingDTO rating;
    private String category;
    private List<SizeDTO> sizes;
    private List<String> specifications;
    private List<ReviewDTO> reviews;
    private DeliveryInfoDTO deliveryInfo;
    //private List<RelatedProductDTO> relatedProducts;
}
