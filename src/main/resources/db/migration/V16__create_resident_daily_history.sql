-- V16__create_resident_daily_history.sql
-- Historial diario por residente, dividido en turno diurno (DAY) y nocturno (NIGHT)

CREATE TABLE resident_daily_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT NOT NULL,
    log_date DATE NOT NULL,
    period VARCHAR(10) NOT NULL,
    comment TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_rdh_resident_date_period (resident_id, log_date, period),
    INDEX idx_rdh_resident_date (resident_id, log_date),
    INDEX idx_rdh_date (log_date),
    CONSTRAINT fk_rdh_resident FOREIGN KEY (resident_id) REFERENCES residents(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
