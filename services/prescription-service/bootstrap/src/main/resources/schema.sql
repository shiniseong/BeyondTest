CREATE TABLE IF NOT EXISTS prescription_codes (
    code VARCHAR(8) PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    activated_for VARCHAR(100),
    activated_at TIMESTAMP,
    expired_at TIMESTAMP
);