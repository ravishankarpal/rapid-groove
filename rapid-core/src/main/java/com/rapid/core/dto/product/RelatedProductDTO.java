package com.rapid.core.dto.product;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatedProductDTO {

    private Long id;
    private String name;
    private String image;
    private Double price;
}
