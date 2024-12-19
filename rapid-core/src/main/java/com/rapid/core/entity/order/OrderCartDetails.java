package com.rapid.core.entity.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rapid.core.dto.cart.CartDetail;
import com.rapid.core.dto.cart.CartItems;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class OrderCartDetails {



    @JsonProperty(value = "shipping_charge")
    private double shippingCharge;

    @JsonProperty(value = "cart_name")
    private String cartName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)

    private List<OrderCartItem> cartItems = new ArrayList<>();

    public OrderCartDetails(CartDetail cartDetail) {
        this.shippingCharge = cartDetail.getShippingCharge();
        this.cartName = cartDetail.getCartName();
        for(CartItems item  : cartDetail.getCartItems()){
            OrderCartItem orderCartItem = new OrderCartItem(item);
            this.cartItems.add(orderCartItem);

        }


    }
}
