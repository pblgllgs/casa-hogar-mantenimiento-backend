CREATE TABLE clinical_record_attachments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clinical_record_id BIGINT NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_name VARCHAR(255),
    file_type VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP NULL,
    updated_by VARCHAR(100),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_attachment_record FOREIGN KEY (clinical_record_id) REFERENCES clinical_records(id)
);

CREATE INDEX idx_attachment_record ON clinical_record_attachments(clinical_record_id);

INSERT INTO clinical_record_attachments (clinical_record_id, file_url, file_name, deleted)
SELECT id, attachments, attachments, FALSE
FROM clinical_records
WHERE attachments IS NOT NULL AND attachments != '' AND attachments LIKE 'http%';

ALTER TABLE clinical_records DROP COLUMN attachments;
