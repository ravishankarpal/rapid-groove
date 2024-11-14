package com.rapid.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttachmentDTO {

    private String fileName;
    private String fileType;
    private byte[] fileData;
}
