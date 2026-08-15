package com.casahogar.mantenimiento.hr.dto;

import com.casahogar.mantenimiento.hr.entity.Shift;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ShiftResponse {

    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private String daysOfWeek;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public static ShiftResponse of(Shift shift) {
        ShiftResponse r = new ShiftResponse();
        r.id = shift.getId();
        r.name = shift.getName();
        r.startTime = shift.getStartTime();
        r.endTime = shift.getEndTime();
        r.daysOfWeek = shift.getDaysOfWeek();
        r.isActive = shift.getIsActive();
        r.createdAt = shift.getCreatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getDaysOfWeek() { return daysOfWeek; }
    public void setDaysOfWeek(String daysOfWeek) { this.daysOfWeek = daysOfWeek; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
