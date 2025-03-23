CREATE TABLE IF NOT EXISTS user_verification_histories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    version VARCHAR(20) NOT NULL,
    os VARCHAR(20) NOT NULL,
    mode VARCHAR(20) NOT NULL,
    hash VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    verified BOOLEAN NOT NULL,
    message VARCHAR(500)
);