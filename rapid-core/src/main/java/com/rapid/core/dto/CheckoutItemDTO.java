package com.rapid.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CheckoutItemDTO {

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotNull(message = "Product name is required")
    private String productName;

    private byte[] picByte;

    @Positive(message = "Quantity must be positive")
    private int quantity;

    @NotNull(message = "Size is required")
    private String size;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

}
