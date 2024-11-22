package com.rapid.core.entity;

import com.rapid.core.dto.CheckoutItemDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "checkout_items")
public class CheckoutItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false, updatable = false)
    private Long id;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "size")
    private String size;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private BigDecimal price;

    @Lob
    @Column(name = "pic_byte",length = 50000000)
    private byte[] picByte;

    public CheckoutItem(CheckoutItemDTO checkoutItemDTO) {
        this.productId = checkoutItemDTO.getProductId();
        this.quantity = checkoutItemDTO.getQuantity();
        this.size = checkoutItemDTO.getSize();
        this.productName = checkoutItemDTO.getProductName();
        this.price = checkoutItemDTO.getPrice();
        this.picByte = checkoutItemDTO.getPicByte();;
    }
}
