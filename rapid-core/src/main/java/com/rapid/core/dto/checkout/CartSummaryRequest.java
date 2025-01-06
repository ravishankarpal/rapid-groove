package com.rapid.core.dto.checkout;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class CartSummaryRequest {

    private double subtotal;
    private double discount;
    private double deliveryFee;
    private double total;
}
