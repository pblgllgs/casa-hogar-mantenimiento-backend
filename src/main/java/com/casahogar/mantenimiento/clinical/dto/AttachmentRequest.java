package com.casahogar.mantenimiento.clinical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AttachmentRequest {

    @NotNull
    private Long clinicalRecordId;

    @NotBlank
    private String fileUrl;

    private String fileName;

    private String fileType;

    public Long getClinicalRecordId() { return clinicalRecordId; }
    public void setClinicalRecordId(Long clinicalRecordId) { this.clinicalRecordId = clinicalRecordId; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}
