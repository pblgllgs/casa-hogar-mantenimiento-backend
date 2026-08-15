package com.casahogar.mantenimiento.hr.dto;

import com.casahogar.mantenimiento.hr.entity.Staff;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StaffResponse {

    private Long id;
    private String employeeCode;
    private Long userId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String documentType;
    private String documentNumber;
    private LocalDate birthDate;
    private String gender;
    private LocalDate hireDate;
    private LocalDate terminationDate;
    private String position;
    private String department;
    private String shift;
    private String phone;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String bankAccount;
    private BigDecimal salary;
    private String status;
    private Boolean isActive;
    private String photoUrl;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

    public static StaffResponse of(Staff staff) {
        StaffResponse r = new StaffResponse();
        r.id = staff.getId();
        r.employeeCode = staff.getEmployeeCode();
        r.userId = staff.getUserId();
        r.firstName = staff.getFirstName();
        r.lastName = staff.getLastName();
        r.fullName = staff.getFirstName() + " " + staff.getLastName();
        r.documentType = staff.getDocumentType() != null ? staff.getDocumentType().name() : null;
        r.documentNumber = staff.getDocumentNumber();
        r.birthDate = staff.getBirthDate();
        r.gender = staff.getGender();
        r.hireDate = staff.getHireDate();
        r.terminationDate = staff.getTerminationDate();
        r.position = staff.getPosition();
        r.department = staff.getDepartment();
        r.shift = staff.getShift() != null ? staff.getShift().name() : null;
        r.phone = staff.getPhone();
        r.emergencyContactName = staff.getEmergencyContactName();
        r.emergencyContactPhone = staff.getEmergencyContactPhone();
        r.bankAccount = staff.getBankAccount();
        r.salary = staff.getSalary();
        r.status = staff.getStatus() != null ? staff.getStatus().name() : null;
        r.isActive = staff.getIsActive();
        r.photoUrl = staff.getPhotoUrl();
        r.createdAt = staff.getCreatedAt();
        r.createdBy = staff.getCreatedBy();
        r.updatedAt = staff.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

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

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public LocalDate getTerminationDate() { return terminationDate; }
    public void setTerminationDate(LocalDate terminationDate) { this.terminationDate = terminationDate; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }

    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
