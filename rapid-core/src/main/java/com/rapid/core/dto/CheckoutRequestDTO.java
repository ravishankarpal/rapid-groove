package com.rapid.core.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutRequestDTO {

    private List<Integer> selectedProductIds;

    private Integer deliveryFee;

}
