package com.casahogar.mantenimiento.hr.dto;

import com.casahogar.mantenimiento.hr.entity.ShiftLog;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShiftLogResponse {

    private Long id;
    private Long shiftId;
    private String shiftName;
    private Long staffId;
    private String staffName;
    private LocalDate logDate;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ShiftLogResponse of(ShiftLog log, String shiftName, String staffName) {
        ShiftLogResponse r = new ShiftLogResponse();
        r.id = log.getId();
        r.shiftId = log.getShiftId();
        r.shiftName = shiftName;
        r.staffId = log.getStaffId();
        r.staffName = staffName;
        r.logDate = log.getLogDate();
        r.comment = log.getComment();
        r.createdAt = log.getCreatedAt();
        r.updatedAt = log.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getShiftId() { return shiftId; }
    public void setShiftId(Long shiftId) { this.shiftId = shiftId; }
    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}