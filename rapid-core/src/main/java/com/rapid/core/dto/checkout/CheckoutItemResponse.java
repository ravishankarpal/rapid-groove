package com.rapid.core.dto.checkout;

import com.rapid.core.entity.checkout.CheckoutItemEntity;
import com.rapid.core.entity.product.ImageModel;
import com.rapid.core.entity.product.ProductDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemResponse {

    private Integer productId;
    private String productName;
    private byte[] productImage;
    private String size;
    private double originalPrice;
    private double currentPrice;
    private Integer quantity;
    private Integer discountPercentage;
    private String deliveryTime;

    public CheckoutItemResponse(CheckoutItemEntity checkoutItemEntity, ProductDetails productDetails) {
        this.productId = checkoutItemEntity.getProductId();
        this.productName = productDetails.getName();
        this.productImage = productDetails.getProductImages().stream().filter(ImageModel::isPrimaryImage).findFirst().get().getPicByte();
        this.size = checkoutItemEntity.getSize();
        this.originalPrice = checkoutItemEntity.getOriginalPrice();
        this.currentPrice = checkoutItemEntity.getCurrentPrice();
        this.quantity = checkoutItemEntity.getQuantity();
        this.discountPercentage = checkoutItemEntity.getDiscountPercentage();
        this.deliveryTime = productDetails.getDeliveryInfo().getStandardDeliveryTime();
    }

}
