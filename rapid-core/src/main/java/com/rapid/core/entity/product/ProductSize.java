package com.rapid.core.entity.product;

import com.rapid.core.dto.product.SizeDTO;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize {
    private String value;
    private Boolean available;

    private ProductPrice price;

    public ProductSize(SizeDTO sizeDTO) {
        this.value = sizeDTO.getValue();
        this.available = sizeDTO.getAvailable();
        this.price = new ProductPrice(sizeDTO.getPrice());
    }
}
