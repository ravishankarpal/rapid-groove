package com.rapid.core.order;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
public class OrderItemDTO {

    private Integer productId;
    private String size;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
