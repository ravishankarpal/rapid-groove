package com.rapid.core.dto.checkout;

import com.rapid.core.entity.checkout.CheckoutDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryResponse {

    private double subtotal;
    private double discount;
    private double deliveryFee;
    private double total;

    public CartSummaryResponse(CheckoutDetails checkoutDetails) {
        this.subtotal = checkoutDetails.getSubtotal();
        this.discount = checkoutDetails.getDiscount();
        this.deliveryFee = checkoutDetails.getDeliveryFee();
        this.total = checkoutDetails.getTotal();
    }
}
