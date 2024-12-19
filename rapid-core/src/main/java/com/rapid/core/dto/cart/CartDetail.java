package com.rapid.core.dto.cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CartDetail {

    @JsonProperty(value = "shipping_charge")
    private double shippingCharge;

    @JsonProperty(value = "cart_name")
    private String cartName;

    @JsonProperty(value = "cart_items")
    private List<CartItems> cartItems;

}
