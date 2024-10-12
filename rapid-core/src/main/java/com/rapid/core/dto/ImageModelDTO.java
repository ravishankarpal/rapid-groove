package com.rapid.core.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageModelDTO {


    private Integer id;
    private String name;

    private String type;

    private  byte[] picByte;
}
