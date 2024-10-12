package com.rapid.core.entity.product;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "product_images")
@Getter
public class ProductImages  implements Serializable {

    @Id
    @Column(name = "product_id")
    private Integer productId;


    @Column(name = "image_id")
    private Integer imageId;


}
