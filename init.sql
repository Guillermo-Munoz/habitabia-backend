CREATE TABLE IF NOT EXISTS users (
                                     id          UUID PRIMARY KEY,
                                     full_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20) NOT NULL,
    bio         TEXT,
    avatar_url  VARCHAR(500),
    active      BOOLEAN DEFAULT true,
    created_at  TIMESTAMPTZ NOT NULL
    );

CREATE TABLE IF NOT EXISTS rooms (
                                     id             UUID PRIMARY KEY,
                                     host_id        UUID NOT NULL REFERENCES users(id),
    title          VARCHAR(255) NOT NULL,
    description    TEXT,
    street         VARCHAR(255),
    city           VARCHAR(100) NOT NULL,
    country        VARCHAR(100) NOT NULL,
    latitude       FLOAT8,
    longitude      FLOAT8,
    price_amount   DECIMAL(10,2) NOT NULL,
    price_currency VARCHAR(3) NOT NULL,
    max_guests     INT NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMPTZ NOT NULL
    );

CREATE TABLE IF NOT EXISTS room_images (
    room_id    UUID NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    image_url  VARCHAR(500) NOT NULL
    );

CREATE TABLE IF NOT EXISTS bookings (
                                        id          UUID PRIMARY KEY,
                                        room_id     UUID NOT NULL REFERENCES rooms(id),
    guest_id    UUID NOT NULL REFERENCES users(id),
    host_id     UUID NOT NULL REFERENCES users(id),
    check_in    DATE NOT NULL,
    check_out   DATE NOT NULL,
    guests      INT NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',
    message     TEXT,
    created_at  TIMESTAMPTZ NOT NULL
    );