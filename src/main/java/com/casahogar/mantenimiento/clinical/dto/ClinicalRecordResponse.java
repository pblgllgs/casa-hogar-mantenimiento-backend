package com.casahogar.mantenimiento.clinical.dto;

import com.casahogar.mantenimiento.clinical.entity.ClinicalRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClinicalRecordResponse {

    private Long id;
    private Long residentId;
    private String residentName;
    private LocalDate recordDate;
    private String recordType;
    private String description;
    private String diagnosis;
    private String treatment;
    private String medication;
    private String dosage;
    private String doctorName;
    private String notes;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

    public static ClinicalRecordResponse of(ClinicalRecord record, String residentName) {
        ClinicalRecordResponse r = new ClinicalRecordResponse();
        r.id = record.getId();
        r.residentId = record.getResidentId();
        r.residentName = residentName;
        r.recordDate = record.getRecordDate();
        r.recordType = record.getRecordType() != null ? record.getRecordType().name() : null;
        r.description = record.getDescription();
        r.diagnosis = record.getDiagnosis();
        r.treatment = record.getTreatment();
        r.medication = record.getMedication();
        r.dosage = record.getDosage();
        r.doctorName = record.getDoctorName();
        r.notes = record.getNotes();
        r.createdAt = record.getCreatedAt();
        r.createdBy = record.getCreatedBy();
        r.updatedAt = record.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getResidentId() { return residentId; }
    public void setResidentId(Long residentId) { this.residentId = residentId; }

    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}