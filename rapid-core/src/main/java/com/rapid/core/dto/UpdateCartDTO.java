package com.rapid.core.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateCartDTO {
    private Integer cartItemId;
    private Integer quantity;
}
