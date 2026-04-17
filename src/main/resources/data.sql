INSERT INTO movie (title, runningTimeMinutes) VALUES ('인터스텔라', 169);
INSERT INTO screening_schedule (movie_id, start_at, end_at) VALUES ((SELECT id FROM movie WHERE title = '인터스텔라' LIMIT 1), '2025-09-20 13:30:00', '2025-09-20 16:19:00');
INSERT INTO screening_schedule (movie_id, start_at, end_at) VALUES ((SELECT id FROM movie WHERE title = '인터스텔라' LIMIT 1), '2025-09-20 18:00:00', '2025-09-20 20:49:00');

INSERT INTO movie (title, runningTimeMinutes) VALUES ('오펜하이머', 180);
INSERT INTO screening_schedule (movie_id, start_at, end_at) VALUES ((SELECT id FROM movie WHERE title = '오펜하이머' LIMIT 1), '2025-09-20 10:00:00', '2025-09-20 13:00:00');
