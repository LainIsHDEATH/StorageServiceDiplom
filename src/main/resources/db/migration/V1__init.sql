CREATE SCHEMA simulations;
SET search_path TO simulations;

CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    username      VARCHAR(128) UNIQUE NOT NULL,
    email         VARCHAR(128) UNIQUE NOT NULL,
    password_hash TEXT
);
CREATE TABLE rooms
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(32)               NOT NULL,
    room_params JSONB                     NOT NULL,
    user_id     INT REFERENCES users (id) NOT NULL
);
CREATE TABLE pid_configs
(
    id           SERIAL PRIMARY KEY,
    kp           DOUBLE PRECISION          NOT NULL,
    ki           DOUBLE PRECISION          NOT NULL,
    kd           DOUBLE PRECISION          NOT NULL,
    tuned_method VARCHAR(32),
    room_id      INT REFERENCES rooms (id) NOT NULL,
    is_active    BOOLEAN DEFAULT TRUE
);
CREATE TABLE simulations
(
    id               SERIAL PRIMARY KEY,
    room_id          INT REFERENCES rooms (id) NOT NULL,
    controller_type  VARCHAR(32),
    status           VARCHAR(32),
    iterations       BIGINT                    NOT NULL DEFAULT 0,
    timestep_seconds INT                       NOT NULL DEFAULT 1
);
CREATE TABLE simulation_events
(
    id             BIGSERIAL PRIMARY KEY,
    simulation_id  INT REFERENCES simulations (id) NOT NULL,
    timestamp      TIMESTAMP,
    temp_in        DOUBLE PRECISION,
    temp_out       DOUBLE PRECISION,
    temp_setpoint  DOUBLE PRECISION,
    heater_power   DOUBLE PRECISION,
    predicted_temp DOUBLE PRECISION,
    is_window_open BOOLEAN,
    is_door_open   BOOLEAN,
    people_count   INT
);
CREATE TABLE models
(
    id          SERIAL PRIMARY KEY,
    room_id     INT REFERENCES rooms (id) NOT NULL,
    type        VARCHAR(16)               NOT NULL,
    path        TEXT,
    description TEXT,
    is_active   BOOLEAN DEFAULT TRUE
);