-- Crear tabla role
DROP TABLE IF EXISTS role;
CREATE TABLE role (
                      id_role INT AUTO_INCREMENT PRIMARY KEY,
                      role_name VARCHAR(255) NOT NULL
);

-- Crear tabla user
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        id_user INT AUTO_INCREMENT PRIMARY KEY,
                        `user` VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        id_role INT,
                        FOREIGN KEY (id_role) REFERENCES role(id_role)
);

-- Crear tabla title
DROP TABLE IF EXISTS title;
CREATE TABLE title (
                       id_title INT AUTO_INCREMENT PRIMARY KEY,
                       title_name VARCHAR(255) NOT NULL
);

-- Crear tabla author
DROP TABLE IF EXISTS author;
CREATE TABLE author (
                        id_author INT AUTO_INCREMENT PRIMARY KEY,
                        author_name VARCHAR(255) NOT NULL,
                        author_last_name VARCHAR(255) NOT NULL
);

-- Crear tabla category
DROP TABLE IF EXISTS category;
CREATE TABLE category (
                          id_category INT AUTO_INCREMENT PRIMARY KEY,
                          category_name VARCHAR(255) NOT NULL
);

-- Crear tabla book
DROP TABLE IF EXISTS book;
CREATE TABLE book (
                      id_book INT AUTO_INCREMENT PRIMARY KEY,
                      id_title INT,
                      id_author INT,
                      id_category INT,
                      available BOOLEAN NOT NULL,
                      FOREIGN KEY (id_title) REFERENCES title(id_title),
                      FOREIGN KEY (id_author) REFERENCES author(id_author),
                      FOREIGN KEY (id_category) REFERENCES category(id_category)
);

-- Crear tabla loan
DROP TABLE IF EXISTS loan;
CREATE TABLE loan (
                      id_loan INT AUTO_INCREMENT PRIMARY KEY,
                      id_user INT,
                      id_book INT,
                      loan_date TIMESTAMP NOT NULL,
                      return_date TIMESTAMP,
                      FOREIGN KEY (id_user) REFERENCES `user`(id_user),
                      FOREIGN KEY (id_book) REFERENCES book(id_book)
);

-- Crear tabla notification
DROP TABLE IF EXISTS notification;
CREATE TABLE notification (
                              id_notification INT AUTO_INCREMENT PRIMARY KEY,
                              id_user INT,
                              message VARCHAR(255) NOT NULL,
                              `date` TIMESTAMP NOT NULL,
                              FOREIGN KEY (id_user) REFERENCES `user`(id_user)
);

-- Insertar datos en la tabla role
INSERT INTO role (role_name) VALUES
                                 ('STUDENT'),
                                 ('TEACHER'),
                                 ('ADMIN');

-- Insertar datos en la tabla user
INSERT INTO `user` (`user`, email, password, id_role) VALUES
                                                          ('aJohnson', 'alice@example.com', 'password123', 1),
                                                          ('bSmith', 'bob@example.com', 'password123', 2),
                                                          ('cWilliams', 'carol@example.com', 'password123', 3),
                                                          ('dBrown', 'david@example.com', 'password123', 1),
                                                          ('eWhite', 'eva@example.com', 'password123', 2);

-- Insertar datos en la tabla title
INSERT INTO title (title_name) VALUES
                                   ('Introduction to Algorithms'),
                                   ('Clean Code'),
                                   ('The Pragmatic Programmer'),
                                   ('Design Patterns'),
                                   ('Artificial Intelligence: A Modern Approach'),
                                   ('Effective Java'),
                                   ('Java Concurrency in Practice'),
                                   ('Refactoring'),
                                   ('Patterns of Enterprise Application Architecture'),
                                   ('Continuous Delivery'),
                                   ('The Mythical Man-Month'),
                                   ('Code Complete'),
                                   ('The Art of Computer Programming'),
                                   ('Domain-Driven Design'),
                                   ('You Don''t Know JS'),
                                   ('Head First Design Patterns'),
                                   ('Cracking the Coding Interview'),
                                   ('The Clean Coder'),
                                   ('Agile Estimating and Planning'),
                                   ('Programming Pearls');

-- Insertar datos en la tabla author
INSERT INTO author (author_name, author_last_name) VALUES
                                                       ('Thomas', 'Cormen'),
                                                       ('Robert', 'Martin'),
                                                       ('Andrew', 'Hunt'),
                                                       ('Erich', 'Gamma'),
                                                       ('Stuart', 'Russell'),
                                                       ('Joshua', 'Bloch'),
                                                       ('Brian', 'Goetz'),
                                                       ('Martin', 'Fowler'),
                                                       ('Kent', 'Beck'),
                                                       ('Jez', 'Humble'),
                                                       ('Frederick', 'Brooks'),
                                                       ('Steve', 'McConnell'),
                                                       ('Donald', 'Knuth'),
                                                       ('Eric', 'Evans'),
                                                       ('Kyle', 'Simpson'),
                                                       ('Eric', 'Freeman'),
                                                       ('Gayle', 'Laakmann McDowell'),
                                                       ('Robert', 'C. Martin'),
                                                       ('Mike', 'Cohn'),
                                                       ('Jon', 'Bentley');

-- Insertar datos en la tabla category
INSERT INTO category (category_name) VALUES
                                         ('Computer Science'),
                                         ('Software Engineering'),
                                         ('Programming'),
                                         ('Design'),
                                         ('Artificial Intelligence'),
                                         ('Best Practices'),
                                         ('Concurrency'),
                                         ('Refactoring'),
                                         ('Architecture'),
                                         ('Delivery'),
                                         ('Project Management'),
                                         ('Code Quality'),
                                         ('Algorithms'),
                                         ('Domain-Driven Design'),
                                         ('JavaScript'),
                                         ('Patterns'),
                                         ('Interview Preparation'),
                                         ('Agile'),
                                         ('Planning'),
                                         ('Optimization');

-- Insertar datos en la tabla book
INSERT INTO book (id_title, id_author, id_category, available) VALUES
                                                                   (1, 1, 1, TRUE),
                                                                   (2, 2, 2, TRUE),
                                                                   (3, 3, 3, TRUE),
                                                                   (4, 4, 4, TRUE),
                                                                   (5, 5, 5, TRUE),
                                                                   (6, 6, 6, TRUE),
                                                                   (7, 7, 7, TRUE),
                                                                   (8, 8, 8, TRUE),
                                                                   (9, 9, 9, TRUE),
                                                                   (10, 10, 10, TRUE),
                                                                   (11, 11, 11, TRUE),
                                                                   (12, 12, 12, TRUE),
                                                                   (13, 13, 13, TRUE),
                                                                   (14, 14, 14, TRUE),
                                                                   (15, 15, 15, TRUE),
                                                                   (16, 16, 16, TRUE),
                                                                   (17, 17, 17, TRUE),
                                                                   (18, 18, 18, TRUE),
                                                                   (19, 19, 19, TRUE),
                                                                   (20, 20, 20, TRUE);

-- Insertar datos en la tabla loan
INSERT INTO loan (id_user, id_book, loan_date, return_date) VALUES
                                                                (1, 1, '2024-01-01 10:00:00', '2024-01-10 10:00:00'),
                                                                (2, 2, '2024-01-02 11:00:00', '2024-01-12 11:00:00'),
                                                                (3, 3, '2024-01-03 12:00:00', '2024-01-13 12:00:00'),
                                                                (4, 4, '2024-01-04 13:00:00', '2024-01-14 13:00:00'),
                                                                (5, 5, '2024-01-05 14:00:00', '2024-01-15 14:00:00');

-- Insertar datos en la tabla notification
INSERT INTO notification (id_user, message, `date`) VALUES
                                                        (1, 'Your book is due soon!', '2024-01-09 09:00:00'),
                                                        (2, 'Your book is due soon!', '2024-01-11 10:00:00'),
                                                        (3, 'Your book is due soon!', '2024-01-12 11:00:00'),
                                                        (4, 'Your book is due soon!', '2024-01-13 12:00:00'),
                                                        (5, 'Your book is due soon!', '2024-01-14 13:00:00');
