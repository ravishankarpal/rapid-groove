package com.rapid.core.dto.product;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    private Double average;
    private Integer totalRatings;
    private Integer totalReviews;
}
