package com.casahogar.mantenimiento.residents.dto;

import com.casahogar.mantenimiento.residents.entity.Resident;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResidentResponse {

    private Long id;
    private String code;
    private String firstName;
    private String lastName;
    private String fullName;
    private String documentType;
    private String documentNumber;
    private LocalDate birthDate;
    private String gender;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private String status;
    private Long roomId;
    private String roomName;
    private String guardianName;
    private String guardianPhone;
    private String guardianEmail;
    private String guardianRelationship;
    private String medicalInfo;
    private String dietaryRestrictions;
    private String notes;
    private String photoUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

    public static ResidentResponse of(Resident resident) {
        ResidentResponse r = new ResidentResponse();
        r.id = resident.getId();
        r.code = resident.getCode();
        r.firstName = resident.getFirstName();
        r.lastName = resident.getLastName();
        r.fullName = resident.getFirstName() + " " + resident.getLastName();
        r.documentType = resident.getDocumentType() != null ? resident.getDocumentType().name() : null;
        r.documentNumber = resident.getDocumentNumber();
        r.birthDate = resident.getBirthDate();
        r.gender = resident.getGender();
        r.entryDate = resident.getEntryDate();
        r.exitDate = resident.getExitDate();
        r.status = resident.getStatus() != null ? resident.getStatus().name() : null;
        r.roomId = resident.getRoomId();
        r.roomName = resident.getRoom() != null ? resident.getRoom().getName() : null;
        r.guardianName = resident.getGuardianName();
        r.guardianPhone = resident.getGuardianPhone();
        r.guardianEmail = resident.getGuardianEmail();
        r.guardianRelationship = resident.getGuardianRelationship();
        r.medicalInfo = resident.getMedicalInfo();
        r.dietaryRestrictions = resident.getDietaryRestrictions();
        r.notes = resident.getNotes();
        r.photoUrl = resident.getPhotoUrl();
        r.isActive = resident.getIsActive();
        r.createdAt = resident.getCreatedAt();
        r.createdBy = resident.getCreatedBy();
        r.updatedAt = resident.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public LocalDate getExitDate() { return exitDate; }
    public void setExitDate(LocalDate exitDate) { this.exitDate = exitDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }

    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) { this.guardianPhone = guardianPhone; }

    public String getGuardianEmail() { return guardianEmail; }
    public void setGuardianEmail(String guardianEmail) { this.guardianEmail = guardianEmail; }

    public String getGuardianRelationship() { return guardianRelationship; }
    public void setGuardianRelationship(String guardianRelationship) { this.guardianRelationship = guardianRelationship; }

    public String getMedicalInfo() { return medicalInfo; }
    public void setMedicalInfo(String medicalInfo) { this.medicalInfo = medicalInfo; }

    public String getDietaryRestrictions() { return dietaryRestrictions; }
    public void setDietaryRestrictions(String dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
