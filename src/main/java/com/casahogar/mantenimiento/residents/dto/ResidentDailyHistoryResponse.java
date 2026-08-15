package com.casahogar.mantenimiento.residents.dto;

import com.casahogar.mantenimiento.residents.entity.ResidentDailyHistory;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResidentDailyHistoryResponse {

    private Long id;
    private Long residentId;
    private LocalDate logDate;
    private String period;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResidentDailyHistoryResponse of(ResidentDailyHistory history) {
        ResidentDailyHistoryResponse r = new ResidentDailyHistoryResponse();
        r.id = history.getId();
        r.residentId = history.getResidentId();
        r.logDate = history.getLogDate();
        r.period = history.getPeriod() != null ? history.getPeriod().name() : null;
        r.comment = history.getComment();
        r.createdAt = history.getCreatedAt();
        r.updatedAt = history.getUpdatedAt();
        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getResidentId() { return residentId; }
    public void setResidentId(Long residentId) { this.residentId = residentId; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
