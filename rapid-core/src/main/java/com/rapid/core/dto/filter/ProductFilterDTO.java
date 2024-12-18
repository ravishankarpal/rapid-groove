package com.rapid.core.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterDTO {

    private String brand;
    private String category;
    private String subCategory;
    private double minPrice;
    private double maxPrice;
    private int minRating;
    private int maxRating;
    private int minDiscount;
    private int maxDiscount;
    private String size;
}
