package com.rapid.core.dto.cart;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartRequestDTO {

    private Integer productId;
    private String selectedSize;
    private Integer quantity;
}
