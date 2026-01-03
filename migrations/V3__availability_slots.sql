CREATE TABLE availability_slots (
                                    id BIGSERIAL PRIMARY KEY,
                                    restaurant_id BIGINT NOT NULL,
                                    date DATE NOT NULL,
                                    start_time TIME NOT NULL,
                                    end_time TIME NOT NULL,
                                    capacity INT NOT NULL,
                                    seats_available INT NOT NULL,
                                    CONSTRAINT fk_availability_restaurant
                                        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
                                            ON DELETE CASCADE,
                                    CONSTRAINT chk_capacity_positive CHECK (capacity > 0),
                                    CONSTRAINT chk_seats_non_negative CHECK (seats_available >= 0),
                                    CONSTRAINT chk_seats_le_capacity CHECK (seats_available <= capacity),
                                    CONSTRAINT chk_time_order CHECK (end_time > start_time),
                                    CONSTRAINT uq_restaurant_date_start UNIQUE (restaurant_id, date, start_time)
);

CREATE INDEX idx_availability_restaurant_date_start
    ON availability_slots(restaurant_id, date, start_time);
