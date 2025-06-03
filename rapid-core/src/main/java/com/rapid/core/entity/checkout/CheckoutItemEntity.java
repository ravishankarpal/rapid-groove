package com.rapid.core.entity.checkout;

import com.rapid.core.dto.checkout.CheckoutItemRequest;
import com.rapid.core.entity.CheckoutItem;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CheckoutItemEntity {

    @Column(name = "product_id", nullable = false)
    private Integer productId;


    @Column(name = "size")
    private String size;

    @Column(name = "original_price", nullable = false)
    private double originalPrice;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "discount_percentage")
    private Integer discountPercentage;

    public CheckoutItemEntity(CheckoutItemRequest checkoutItemRequest) {
        this.productId = checkoutItemRequest.getProductId();
        this.size = checkoutItemRequest.getSize();
        this.originalPrice = checkoutItemRequest.getOriginalPrice();
        this.currentPrice = checkoutItemRequest.getCurrentPrice();
        this.quantity = checkoutItemRequest.getQuantity();
        this.discountPercentage = checkoutItemRequest.getDiscountPercentage();

    }
}
