package com.rapid.core.entity.order;

import com.rapid.core.dto.cart.CartItems;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderProductDetails {

    private String productId;
    private String productName;
    private int quantity;
    private String size;
    private double originalUnitPrice;
    private double discountedUnitPrice;
    public OrderProductDetails(CartItems items) {
        this.productId = items.getItemId();
        this.productName = items.getItemName();
        this.quantity = items.getItemQuantity();
        this.size = String.join(",",items.getItemTags() );
        this.originalUnitPrice = items.getItemOriginalUnitPrice();
        this.discountedUnitPrice = items.getItemDiscountedUnitPrice();
    }
}
