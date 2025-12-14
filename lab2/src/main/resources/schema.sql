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