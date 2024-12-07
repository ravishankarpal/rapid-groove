package com.rapid.core.entity.product;


import com.rapid.core.dto.ProductDetailDTO;
import com.rapid.core.dto.product.ImagesDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "image_model")

@AllArgsConstructor
public class ImageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "pic_byte",length = 50000000)
    private  byte[] picByte;

    @Column(name = "primary_image", length = 1)
    private boolean isPrimaryImage;

    public ImageModel(String name, String type, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;

    }


    public ImageModel(ImagesDTO imagesDTO){
        this.name = imagesDTO.getName();
        this.type = imagesDTO.getType();
        this.picByte = imagesDTO.getPicByte();
    }


    public ImageModel(ProductDetailDTO productDetailDTO){

    }
}
