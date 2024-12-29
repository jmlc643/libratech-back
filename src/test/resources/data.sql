-- Insertar datos en la tabla roles
INSERT INTO roles (role_name) VALUES
                                  ('STUDENT'),
                                  ('TEACHER'),
                                  ('ADMIN');

-- Insertar datos en la tabla users
INSERT INTO users (user, email, password, role_id) VALUES
                                                       ('aJohnson', 'alice@example.com', 'password123', 1),
                                                       ('bSmith', 'bob@example.com', 'password123', 2),
                                                       ('cWilliams', 'carol@example.com', 'password123', 3),
                                                       ('dBrown', 'david@example.com', 'password123', 1),
                                                       ('eWhite', 'eva@example.com', 'password123', 2);

-- Insertar datos en la tabla titles
INSERT INTO titles (title_name) VALUES
                                    ('Introduction to Algorithms'),
                                    ('Clean Code'),
                                    ('The Pragmatic Programmer'),
                                    ('Design Patterns'),
                                    ('Artificial Intelligence: A Modern Approach');

-- Insertar datos en la tabla authors
INSERT INTO authors (author_name, author_last_name) VALUES
                                      ('Thomas', 'Cormen'),
                                      ('Robert', 'Martin'),
                                      ('Andrew', 'Hunt'),
                                      ('Erich', 'Gamma'),
                                      ('Stuart', 'Russell');

-- Insertar datos en la tabla categories
INSERT INTO categories (category_name) VALUES
                                           ('Computer Science'),
                                           ('Software Engineering'),
                                           ('Programming'),
                                           ('Design'),
                                           ('Artificial Intelligence');

-- Insertar datos en la tabla books
INSERT INTO books (title_id, author_id, category_id, available) VALUES
                                                                    (1, 1, 1, TRUE),
                                                                    (2, 2, 2, TRUE),
                                                                    (3, 3, 3, TRUE),
                                                                    (4, 4, 4, TRUE),
                                                                    (5, 5, 5, TRUE);

-- Insertar datos en la tabla loans
INSERT INTO loans (user_id, book_id, loan_date, return_date) VALUES
                                                                 (1, 1, '2024-01-01 10:00:00', '2024-01-10 10:00:00'),
                                                                 (2, 2, '2024-01-02 11:00:00', '2024-01-12 11:00:00'),
                                                                 (3, 3, '2024-01-03 12:00:00', '2024-01-13 12:00:00'),
                                                                 (4, 4, '2024-01-04 13:00:00', '2024-01-14 13:00:00'),
                                                                 (5, 5, '2024-01-05 14:00:00', '2024-01-15 14:00:00');

-- Insertar datos en la tabla notifications
INSERT INTO notifications (user_id, message, date) VALUES
                                                       (1, 'Your book is due soon!', '2024-01-09 09:00:00'),
                                                       (2, 'Your book is due soon!', '2024-01-11 10:00:00'),
                                                       (3, 'Your book is due soon!', '2024-01-12 11:00:00'),
                                                       (4, 'Your book is due soon!', '2024-01-13 12:00:00'),
                                                       (5, 'Your book is due soon!', '2024-01-14 13:00:00');
