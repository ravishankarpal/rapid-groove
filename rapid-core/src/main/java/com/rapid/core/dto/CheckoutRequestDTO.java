package com.rapid.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequestDTO {

    @NotNull(message = "Item should not be blank")
    private List<CheckoutItemDTO> checkoutItems;
    @NotNull(message = "Total amount should not be blank")
    private BigDecimal totalAmount;
    private Double discountAmount;
}
