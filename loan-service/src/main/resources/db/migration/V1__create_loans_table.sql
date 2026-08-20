CREATE TABLE loans (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    interest_rate NUMERIC(5, 2) NOT NULL,
    tenure_months INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);