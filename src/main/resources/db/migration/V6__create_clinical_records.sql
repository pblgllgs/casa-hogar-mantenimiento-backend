CREATE TABLE clinical_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT NOT NULL,
    record_date DATE NOT NULL,
    record_type VARCHAR(30) NOT NULL,
    description TEXT,
    diagnosis TEXT,
    treatment TEXT,
    medication VARCHAR(255),
    dosage VARCHAR(100),
    doctor_name VARCHAR(200),
    notes TEXT,
    attachments VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at DATETIME,
    updated_by VARCHAR(100),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_clinical_record_resident FOREIGN KEY (resident_id) REFERENCES residents(id)
);

CREATE INDEX idx_clinical_records_resident ON clinical_records(resident_id);
CREATE INDEX idx_clinical_records_date ON clinical_records(record_date);
CREATE INDEX idx_clinical_records_type ON clinical_records(record_type);