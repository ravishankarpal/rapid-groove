package com.rapid.core.dto;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddToCartRequestDTO {

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "SizePrice ID is required")
    private Integer sizePriceId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
