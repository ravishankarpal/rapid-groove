package com.rapid.core.dto.product;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeDTO {

    private String value;
    private Boolean available;
    private PriceDTO price;
}
