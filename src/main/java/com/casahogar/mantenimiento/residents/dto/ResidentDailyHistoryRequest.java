package com.casahogar.mantenimiento.residents.dto;

import java.time.LocalDate;

public class ResidentDailyHistoryRequest {

    private LocalDate logDate;
    private String period;
    private String comment;

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
