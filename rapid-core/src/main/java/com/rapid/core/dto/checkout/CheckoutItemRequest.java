package com.rapid.core.dto.checkout;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class CheckoutItemRequest {
    private Integer productId;
    private String size;
    private double originalPrice;
    private double currentPrice;
    private Integer quantity;
    private Integer discountPercentage;
}
