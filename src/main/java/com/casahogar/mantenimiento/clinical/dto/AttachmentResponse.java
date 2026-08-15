package com.casahogar.mantenimiento.clinical.dto;

import com.casahogar.mantenimiento.clinical.entity.ClinicalRecordAttachment;

import java.time.LocalDateTime;

public class AttachmentResponse {

    private Long id;
    private Long clinicalRecordId;
    private String fileUrl;
    private String fileName;
    private String fileType;
    private LocalDateTime createdAt;

    public static AttachmentResponse of(ClinicalRecordAttachment entity) {
        AttachmentResponse r = new AttachmentResponse();
        r.id = entity.getId();
        r.clinicalRecordId = entity.getClinicalRecordId();
        r.fileUrl = entity.getFileUrl();
        r.fileName = entity.getFileName();
        r.fileType = entity.getFileType();
        r.createdAt = entity.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClinicalRecordId() { return clinicalRecordId; }
    public void setClinicalRecordId(Long clinicalRecordId) { this.clinicalRecordId = clinicalRecordId; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
