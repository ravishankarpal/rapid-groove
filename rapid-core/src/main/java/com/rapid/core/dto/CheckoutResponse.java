package com.rapid.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CheckoutResponse {

    private Integer productId;
    private String productName;
    private Integer quantity;
    private String size;
    private BigDecimal price;
    private byte[] picByte;
    private BigDecimal totalAmount;
    private Double discountAmount;

}
