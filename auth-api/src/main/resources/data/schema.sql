CREATE TABLE IF NOT EXISTS public.users
(
    id            UUID PRIMARY KEY,
    login         VARCHAR(255) UNIQUE NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL
);
CREATE TABLE IF NOT EXISTS public.processing_log
(
    id          UUID PRIMARY KEY,
    user_id     UUID,
    input_text  TEXT,
    output_text TEXT,
    created_at  TIMESTAMP
);
