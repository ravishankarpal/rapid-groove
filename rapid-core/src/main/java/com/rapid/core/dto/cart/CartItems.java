package com.rapid.core.dto.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {

    @JsonProperty(value = "item_id")
    private String itemId;

    @JsonProperty(value = "item_name")
    private String itemName;

    @JsonProperty(value = "item_description")
    private String itemDescription;

    @JsonProperty(value = "item_tags")
    private List<String> itemTags;

    @JsonProperty(value = "item_details_url")
    private String itemDetailsUrl;

    @JsonProperty(value = "item_image_url")
    private String itemImageUrl;

    @JsonProperty(value = "item_original_unit_price")
    private double itemOriginalUnitPrice;

    @JsonProperty(value = "item_discounted_unit_price")
    private double itemDiscountedUnitPrice;

    @JsonProperty(value = "item_currency")
    private String itemCurrency;


    @JsonProperty(value = "item_quantity")
    private int itemQuantity;


}
