CREATE TABLE IF NOT EXISTS authors (
    id BIGSERIAL PRIMARY KEY,
    fio VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    birth_date VARCHAR(10),
    description TEXT
);

CREATE TABLE IF NOT EXISTS books (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    publication_year VARCHAR(4),
    page_num INTEGER,
    rating DOUBLE PRECISION CHECK (rating >= 0 AND rating <= 10),
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(20) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    changed_by VARCHAR(100) DEFAULT 'anonymous',
    change_details JSONB,
    event_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON COLUMN audit_log.event_type IS 'CREATED, UPDATED, DELETED';
COMMENT ON COLUMN audit_log.entity_type IS 'Author или Book';
COMMENT ON COLUMN audit_log.change_details IS 'JSON с деталями изменений';