package com.casahogar.mantenimiento.hr.dto;

import java.time.LocalDate;

public class ShiftLogRequest {

    private Long shiftId;
    private Long staffId;
    private LocalDate logDate;
    private String comment;

    public Long getShiftId() { return shiftId; }
    public void setShiftId(Long shiftId) { this.shiftId = shiftId; }
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}