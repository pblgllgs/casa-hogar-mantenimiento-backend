package com.casahogar.mantenimiento.hr.dto;

import com.casahogar.mantenimiento.hr.entity.StaffShift;
import java.time.LocalDate;

public class StaffShiftResponse {

    private Long id;
    private Long staffId;
    private String staffName;
    private Long shiftId;
    private String shiftName;
    private LocalDate startDate;
    private Boolean isActive;

    public static StaffShiftResponse of(StaffShift ss) {
        StaffShiftResponse r = new StaffShiftResponse();
        r.id = ss.getId();
        r.staffId = ss.getStaffId();
        r.shiftId = ss.getShiftId();
        r.startDate = ss.getStartDate();
        r.isActive = ss.getIsActive();
        return r;
    }

    public static StaffShiftResponse of(StaffShift ss, String staffName, String shiftName) {
        StaffShiftResponse r = of(ss);
        r.staffName = staffName;
        r.shiftName = shiftName;
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public Long getShiftId() { return shiftId; }
    public void setShiftId(Long shiftId) { this.shiftId = shiftId; }
    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}