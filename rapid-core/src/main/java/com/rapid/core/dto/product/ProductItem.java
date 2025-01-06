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
    private String size;

    public ProductItem(OrderProductDetails orderProductDetails) {
        this.productId = orderProductDetails.getProductId();
        this.productName = orderProductDetails.getProductName();
        this.quantity = orderProductDetails.getQuantity();
        this.price = orderProductDetails.getOriginalUnitPrice();
        Integer discountAmt = (int) orderProductDetails.getDiscountedUnitPrice();
        this.discount = discountAmt;
        this.size=orderProductDetails.getSize();

        //this.image = orderProductDetails.getProductId()

    }
}
