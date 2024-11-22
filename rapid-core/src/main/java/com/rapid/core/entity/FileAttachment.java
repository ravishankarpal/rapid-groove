package com.rapid.core.entity;


import com.rapid.core.dto.AttachmentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "file_attachments")
public class FileAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Lob
    @Column(name = "file_data", nullable = false,length = 50000000)
    private byte[] fileData;

    public FileAttachment(AttachmentDTO attachmentDTO){
        this.fileName = attachmentDTO.getFileName();
        this.fileType = attachmentDTO.getFileType();
        this.fileData = attachmentDTO.getFileData();
    }
}
