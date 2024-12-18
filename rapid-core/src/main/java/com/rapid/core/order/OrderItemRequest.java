package com.rapid.core.order;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@Getter
@Setter
public class OrderItemRequest {

    @NotNull
    private Integer productId;

    private String size;

    @Min(1)
    private Integer quantity;

    private double subTotalAmount;

    private double discount;

    private double totalAmount;
}
