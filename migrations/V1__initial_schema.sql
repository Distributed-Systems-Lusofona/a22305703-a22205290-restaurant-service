CREATE TABLE restaurants (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(120) NOT NULL,
                             address TEXT,
                             city VARCHAR(80) NOT NULL,
                             country VARCHAR(80) NOT NULL,
                             phone VARCHAR(30) NOT NULL,
                             email VARCHAR(255) NOT NULL
);

CREATE INDEX idx_restaurants_name ON restaurants(name);
