-- Crear tabla role
DROP TABLE IF EXISTS role;
CREATE TABLE role (
                      id_role INT AUTO_INCREMENT PRIMARY KEY,
                      role_name VARCHAR(255) NOT NULL
);

-- Crear tabla users
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id_user INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       id_role INT,
                       FOREIGN KEY (id_role) REFERENCES role(id_role)
);

-- Crear tabla book
DROP TABLE IF EXISTS book;
CREATE TABLE book (
                      id_book INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      category VARCHAR(255) NOT NULL,
                      available BOOLEAN NOT NULL
);

-- Crear tabla loan
DROP TABLE IF EXISTS loan;
CREATE TABLE loan (
                      id_loan INT AUTO_INCREMENT PRIMARY KEY,
                      id_user INT,
                      id_book INT,
                      loan_date TIMESTAMP NOT NULL,
                      return_date TIMESTAMP,
                      FOREIGN KEY (id_user) REFERENCES users(id_user),
                      FOREIGN KEY (id_book) REFERENCES book(id_book)
);

-- Crear tabla notification
DROP TABLE IF EXISTS notification;
CREATE TABLE notification (
                              id_notification INT AUTO_INCREMENT PRIMARY KEY,
                              id_user INT,
                              message VARCHAR(255) NOT NULL,
                              `date` TIMESTAMP NOT NULL,
                              FOREIGN KEY (id_user) REFERENCES users(id_user)
);

-- Insertar datos en la tabla role
INSERT INTO role (role_name) VALUES
                                 ('STUDENT'),
                                 ('TEACHER'),
                                 ('ADMIN');

-- Insertar datos en la tabla users
INSERT INTO users (username, email, password, id_role) VALUES
                                                           ('aJohnson', 'alice@example.com', 'password123', 1),
                                                           ('bSmith', 'bob@example.com', 'password123', 2),
                                                           ('cWilliams', 'carol@example.com', 'password123', 3),
                                                           ('dBrown', 'david@example.com', 'password123', 1),
                                                           ('eWhite', 'eva@example.com', 'password123', 2);

-- Insertar datos en la tabla book
INSERT INTO book (title, author, category, available) VALUES
                                                          ('Introduction to Algorithms', 'Thomas Cormen', 'Computer Science', TRUE),
                                                          ('Clean Code', 'Robert Martin', 'Software Engineering', TRUE),
                                                          ('The Pragmatic Programmer', 'Andrew Hunt', 'Programming', TRUE),
                                                          ('Design Patterns', 'Erich Gamma', 'Design', TRUE),
                                                          ('Artificial Intelligence: A Modern Approach', 'Stuart Russell', 'Artificial Intelligence', TRUE),
                                                          ('Effective Java', 'Joshua Bloch', 'Best Practices', TRUE),
                                                          ('Java Concurrency in Practice', 'Brian Goetz', 'Concurrency', TRUE),
                                                          ('Refactoring', 'Martin Fowler', 'Refactoring', TRUE),
                                                          ('Patterns of Enterprise Application Architecture', 'Martin Fowler', 'Architecture', TRUE),
                                                          ('Continuous Delivery', 'Jez Humble', 'Delivery', TRUE),
                                                          ('The Mythical Man-Month', 'Frederick Brooks', 'Project Management', TRUE),
                                                          ('Code Complete', 'Steve McConnell', 'Code Quality', TRUE),
                                                          ('The Art of Computer Programming', 'Donald Knuth', 'Algorithms', TRUE),
                                                          ('Domain-Driven Design', 'Eric Evans', 'Domain-Driven Design', TRUE),
                                                          ('You Dont Know JS', 'Kyle Simpson', 'JavaScript', TRUE),
    ('Head First Design Patterns', 'Eric Freeman', 'Patterns', TRUE),
    ('Cracking the Coding Interview', 'Gayle Laakmann McDowell', 'Interview Preparation', TRUE),
    ('The Clean Coder', 'Robert C. Martin', 'Best Practices', TRUE),
    ('Agile Estimating and Planning', 'Mike Cohn', 'Agile', TRUE),
    ('Programming Pearls', 'Jon Bentley', 'Optimization', TRUE);

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
