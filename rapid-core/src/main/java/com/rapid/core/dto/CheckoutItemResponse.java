package com.rapid.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CheckoutItemResponse {
    private Integer productId;
    private String productName;
    private Integer quantity;
    private String size;
    private BigDecimal price;
    private byte[] picByte;
}
