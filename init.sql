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

CREATE TABLE IF NOT EXISTS reviews (
    id              UUID PRIMARY KEY,
    booking_id      UUID NOT NULL REFERENCES bookings(id),
    reviewer_id     UUID NOT NULL REFERENCES users(id),
    rating          INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment         VARCHAR(1000),
    created_at      TIMESTAMPTZ NOT NULL,
    is_review_for_host  BOOLEAN NOT NULL DEFAULT false,
    is_public       BOOLEAN NOT NULL DEFAULT true,
    is_approved     BOOLEAN NOT NULL DEFAULT false,
    is_deleted      BOOLEAN NOT NULL DEFAULT false,
    is_flagged      BOOLEAN NOT NULL DEFAULT false,
    is_verified     BOOLEAN NOT NULL DEFAULT false,
    is_edited       BOOLEAN NOT NULL DEFAULT false,
    is_responded    BOOLEAN NOT NULL DEFAULT false,
    approved_at     TIMESTAMPTZ,
    flagged_at      TIMESTAMPTZ,
    flag_reason     VARCHAR(255),
    edited_at       TIMESTAMPTZ,
    edit_count      INT NOT NULL DEFAULT 0,
    host_response   VARCHAR(1000),
    responded_at    TIMESTAMPTZ,
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS banned_words (
    id          UUID PRIMARY KEY,
    word        VARCHAR(100) NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS conversations (
    id          UUID PRIMARY KEY,
    booking_id  UUID NOT NULL UNIQUE REFERENCES bookings(id),
    guest_id    UUID NOT NULL REFERENCES users(id),
    host_id     UUID NOT NULL REFERENCES users(id),
    created_at  TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS messages (
    id               UUID PRIMARY KEY,
    conversation_id  UUID NOT NULL REFERENCES conversations(id),
    sender_id        UUID NOT NULL REFERENCES users(id),
    content          VARCHAR(2000) NOT NULL,
    sent_at          TIMESTAMPTZ NOT NULL,
    is_read          BOOLEAN NOT NULL DEFAULT false
);