-- Create app_user table
CREATE TABLE IF NOT EXISTS app_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
