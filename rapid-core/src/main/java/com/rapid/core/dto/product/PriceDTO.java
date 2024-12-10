package com.rapid.core.dto.product;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {
    private Double current;
    private Double original;
    private Integer discountPercentage;
}
