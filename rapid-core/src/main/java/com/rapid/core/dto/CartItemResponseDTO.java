package com.rapid.core.dto;

import com.rapid.core.entity.product.ProductSizePrice;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {

    private Integer id;

    private List<CartProduct> product;

    private List<ProductSizePrice> productSizePrice;



}