package com.casahogar.mantenimiento.clinical.entity;

import com.casahogar.mantenimiento.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "clinical_record_attachments")
public class ClinicalRecordAttachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinical_record_id", nullable = false)
    private Long clinicalRecordId;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_type", length = 50)
    private String fileType;

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
}
