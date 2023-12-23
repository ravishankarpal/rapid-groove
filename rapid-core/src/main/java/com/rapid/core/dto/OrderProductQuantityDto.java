package com.rapid.core.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderProductQuantityDto {


    private Integer productId;

    private Integer quantity;

    public OrderProductQuantityDto(Integer quantity) {
        this.quantity = quantity;
    }
}
