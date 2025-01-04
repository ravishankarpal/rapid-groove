package com.rapid.core.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {

    private Integer productId;

    private String productName;
    private double price;
    private byte image[];
    private String lastPurchased;
    private String inStock;


}
