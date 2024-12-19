package com.rapid.core.entity.order;

import com.rapid.core.dto.cart.CartItems;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "order_cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class OrderCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "item_id", length = 50)
    private String itemId;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(name = "item_description", length = 500)
    private String itemDescription;

    @ElementCollection
    @CollectionTable(name = "item_tags", joinColumns = @JoinColumn(name = "order_cart_item_id"))
    @Column(name = "tag")
    private List<String> itemTags;

    @Column(name = "item_details_url", length = 255)
    private String itemDetailsUrl;

    @Column(name = "item_image_url", length = 255)
    private String itemImageUrl;

    @Column(name = "item_original_unit_price")
    private double itemOriginalUnitPrice;

    @Column(name = "item_discounted_unit_price")
    private double itemDiscountedUnitPrice;

    @Column(name = "item_currency", length = 10)
    private String itemCurrency;

    @Column(name = "item_quantity")
    private int itemQuantity;

    public OrderCartItem(CartItems item) {

        this.itemId = item.getItemId();
        this.itemName = item.getItemName();
        this.itemDescription = item.getItemDescription();
        this.itemTags = item.getItemTags();
        this.itemDetailsUrl = item.getItemDetailsUrl();
        this.itemImageUrl = item.getItemImageUrl();
        this.itemOriginalUnitPrice = item.getItemOriginalUnitPrice();
        this.itemDiscountedUnitPrice = item.getItemDiscountedUnitPrice();
        this.itemCurrency = item.getItemCurrency();
        this.itemQuantity = item.getItemQuantity();


    }
}
