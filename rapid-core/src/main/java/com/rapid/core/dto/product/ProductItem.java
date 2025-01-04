package com.rapid.core.dto.product;


import com.rapid.core.entity.order.OrderProductDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {

    private String productId;

    private String productName;

    private Integer quantity;

    private double price;
    private double discount;

    private byte image[];

    public ProductItem(OrderProductDetails orderProductDetails) {
        this.productId = orderProductDetails.getProductId();
        this.productName = orderProductDetails.getProductName();
        this.quantity = orderProductDetails.getQuantity();
        this.price = orderProductDetails.getOriginalUnitPrice();
        this.discount = orderProductDetails.getDiscountedUnitPrice();

        //this.image = orderProductDetails.getProductId()

    }
}
