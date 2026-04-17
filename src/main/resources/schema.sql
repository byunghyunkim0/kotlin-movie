CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    runningTimeMinutes INT NOT NULL
);

CREATE TABLE IF NOT EXISTS screening_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    used_points INT NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    total_price INT NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservations_id BIGINT NOT NULL,
    screening_id BIGINT NOT NULL,
    FOREIGN KEY (reservations_id) REFERENCES reservations(id),
    FOREIGN KEY (screening_id) REFERENCES screening_schedule(id)
);

CREATE TABLE IF NOT EXISTS reserved_seat (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES reservation_item(id)
);
