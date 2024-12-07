package com.rapid.core.entity.product;


import com.rapid.core.dto.product.RatingDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductRating {


    private Double average;
    private Integer totalRatings;
    private Integer totalReviews;

    public ProductRating(RatingDTO rating) {
        this.average = rating.getAverage();
        this.totalRatings = rating.getTotalRatings();
        this.totalReviews = rating.getTotalReviews();
    }

    public ProductRating(ProductRating rating) {

        this.average = rating.getAverage();
        this.totalRatings = rating.getTotalRatings();
        this.totalReviews = rating.getTotalReviews();
    }
}
