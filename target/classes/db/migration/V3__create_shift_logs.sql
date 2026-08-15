CREATE TABLE shift_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shift_id BIGINT NOT NULL,
    staff_id BIGINT NOT NULL,
    log_date DATE NOT NULL,
    comment TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shift_id) REFERENCES shifts(id),
    FOREIGN KEY (staff_id) REFERENCES staff(id),
    INDEX idx_sl_date (log_date),
    INDEX idx_sl_shift_date (shift_id, log_date)
);