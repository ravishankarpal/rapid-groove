package com.rapid.core.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplainDTO {
    private String userId;
    private String orderNumber;
    private String department;
    private String subject;
    private String message;
    private AttachmentDTO attachment;
}
