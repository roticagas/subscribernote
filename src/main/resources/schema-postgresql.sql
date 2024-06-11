-- please run sql manually in db

CREATE TABLE subscriber_model
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         TEXT,
    title        TEXT,
    description  TEXT,
    status       TEXT,
    recurring_at TIMESTAMPTZ,
    created_at   TIMESTAMPTZ,
    modified_at  TIMESTAMPTZ
);

CREATE TABLE config_model
(
    id          SERIAL PRIMARY KEY,
    config_key   VARCHAR(255),
    config_value VARCHAR(255),
    created_at   TIMESTAMP,
    modified_at  TIMESTAMP
);

CREATE INDEX idx_subscriber_model_name_title ON subscriber_model (name, title);