CREATE TABLE menu_items (
                            id BIGSERIAL PRIMARY KEY,
                            restaurant_id BIGINT NOT NULL,
                            name VARCHAR(120) NOT NULL,
                            description VARCHAR(500),
                            price NUMERIC(12,2) NOT NULL,
                            currency VARCHAR(3) NOT NULL,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            CONSTRAINT fk_menu_items_restaurant
                                FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
                                    ON DELETE CASCADE
);

CREATE INDEX idx_menu_items_restaurant_id ON menu_items(restaurant_id);
