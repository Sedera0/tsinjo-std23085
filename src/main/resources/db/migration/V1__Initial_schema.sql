CREATE DATABASE tsinjo_db;

\c tsinjo_db;

DROP TABLE IF EXISTS help CASCADE;
DROP TABLE IF EXISTS donation CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS beneficiary CASCADE;
DROP TABLE IF EXISTS donor CASCADE;

CREATE TABLE donor (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       created_at TIMESTAMPTZ DEFAULT NOW(),
                       CONSTRAINT uk_donor_email UNIQUE (email)
);

CREATE TABLE beneficiary (
                             id SERIAL PRIMARY KEY,
                             email VARCHAR(255) NOT NULL,
                             full_name VARCHAR(255) NOT NULL,
                             created_at TIMESTAMPTZ DEFAULT NOW(),
                             CONSTRAINT uk_beneficiary_email UNIQUE (email)
);

CREATE TABLE payment (
                         id VARCHAR(36) PRIMARY KEY,  -- ID de transaction PSP
                         psp_type VARCHAR(20) NOT NULL CHECK (psp_type IN ('ORANGE_MONEY', 'MVOLA')),
                         amount INTEGER NOT NULL CHECK (amount > 0),
                         status VARCHAR(20) NOT NULL CHECK (status IN ('VERIFYING', 'SUCCEEDED', 'FAILED')),
                         created_at TIMESTAMPTZ DEFAULT NOW(),
                         last_updated TIMESTAMPTZ,
                         verification_attempts INTEGER DEFAULT 0
);

CREATE TABLE donation (
                          id SERIAL PRIMARY KEY,
                          donor_id INTEGER NOT NULL REFERENCES donor(id),
                          payment_id VARCHAR(36) NOT NULL REFERENCES payment(id),
                          created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE help (
                      id SERIAL PRIMARY KEY,
                      beneficiary_id INTEGER NOT NULL REFERENCES beneficiary(id),
                      payment_id VARCHAR(36) NOT NULL REFERENCES payment(id),
                      accident_description TEXT NOT NULL,
                      created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_payment_status ON payment(status);
CREATE INDEX idx_donation_date ON donation(created_at);
CREATE INDEX idx_help_date ON help(created_at);