package com.rapid.core.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CartDetailResponse {

    @JsonProperty(value = "cart_id")
    private String cartId;


}
