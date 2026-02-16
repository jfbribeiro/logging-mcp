CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    prompt TEXT NOT NULL,
    username VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    response TEXT,
    tokens INTEGER
);

CREATE INDEX idx_audit_log_username ON audit_log (username);
CREATE INDEX idx_audit_log_timestamp ON audit_log (timestamp);
