package com.rapid.core.dto.product;

import java.util.List;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor

public class ImagesDTO {
    private String name;
//    private String primaryImage;
//    private List<ImagesDTO> thumbnails;
    private String type;
    private  byte[] picByte;


    public ImagesDTO(String name, String type, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }
}
