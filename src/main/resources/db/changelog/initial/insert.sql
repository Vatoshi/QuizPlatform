INSERT INTO users (username, password, email, enabled, role_id) VALUES
                                                      ('john_doe', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', 'john@example.com',true,1),
                                                      ('jane_smith', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', 'jane@example.com',true,1),
                                                      ('mike_jones', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', 'mike@example.com',true,1),
                                                      ('sarah_connor', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', 'sarah@example.com',true,1),
                                                      ('alex_river', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', 'alex@example.com',true,1);
                                                        --пароль для всех qwe

INSERT INTO categories (id, category_name) VALUES
                                      (1, 'Math'),
                                      (2, 'Science'),
                                      (3, 'History'),
                                      (4, 'Geography'),
                                      (5, 'Technology');

INSERT INTO quizzes ( title, description, creator_id, category_id) VALUES
                                                                          ( 'Algebra Basics', 'Test your algebra skills!', 1, 1),
                                                                          ( 'Physics Essentials', 'Fundamental physics questions.', 2, 2),
                                                                          ( 'World History', 'How well do you know historical events?', 3, 3),
                                                                          ( 'Capitals of the World', 'Match countries with their capitals.', 4, 4),
                                                                          ( 'Programming 101', 'Basic programming concepts.', 5, 5);

INSERT INTO questions (id, quiz_id, question_text) VALUES
                                                       (1, 1, 'What is 2 + 2?'),
                                                       (2, 1, 'Solve for x: 3x = 9'),
                                                       (3, 1, 'What is the square root of 16?');

INSERT INTO questions (id, quiz_id, question_text) VALUES
                                                       (4, 2, 'What is the speed of light?'),
                                                       (5, 2, 'Who formulated the law of universal gravitation?'),
                                                       (6, 2, 'What is the unit of force?');

INSERT INTO questions (id, quiz_id, question_text) VALUES
                                                       (7, 3, 'Who was the first President of the United States?'),
                                                       (8, 3, 'In what year did World War II end?'),
                                                       (9, 3, 'Which empire was ruled by Julius Caesar?');

INSERT INTO questions (id, quiz_id, question_text) VALUES
                                                       (10, 4, 'What is the capital of France?'),
                                                       (11, 4, 'What is the capital of Japan?'),
                                                       (12, 4, 'What is the capital of Brazil?');

INSERT INTO questions (id, quiz_id, question_text) VALUES
                                                       (13, 5, 'What does HTML stand for?'),
                                                       (14, 5, 'Which programming language is known for machine learning?'),
                                                       (15, 5, 'What is the main purpose of a loop in programming?');

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (1, 1, '3', false),
                                                                   (2, 1, '4', true),
                                                                   (3, 1, '5', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (4, 2, 'x = 2', false),
                                                                   (5, 2, 'x = 3', true),
                                                                   (6, 2, 'x = 4', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (7, 3, '2', false),
                                                                   (8, 3, '4', true),
                                                                   (9, 3, '8', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (10, 4, '300,000 km/s', true),
                                                                   (11, 4, '150,000 km/s', false),
                                                                   (12, 4, '1,000,000 km/s', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (13, 5, 'Albert Einstein', false),
                                                                   (14, 5, 'Isaac Newton', true),
                                                                   (15, 5, 'Galileo Galilei', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (16, 6, 'Newton', true),
                                                                   (17, 6, 'Joule', false),
                                                                   (18, 6, 'Watt', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (19, 7, 'George Washington', true),
                                                                   (20, 7, 'Thomas Jefferson', false),
                                                                   (21, 7, 'Abraham Lincoln', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (22, 8, '1943', false),
                                                                   (23, 8, '1945', true),
                                                                   (24, 8, '1950', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (25, 9, 'Roman Empire', true),
                                                                   (26, 9, 'Greek Empire', false),
                                                                   (27, 9, 'Persian Empire', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (28, 10, 'London', false),
                                                                   (29, 10, 'Berlin', false),
                                                                   (30, 10, 'Paris', true);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (31, 11, 'Beijing', false),
                                                                   (32, 11, 'Seoul', false),
                                                                   (33, 11, 'Tokyo', true);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (34, 12, 'Rio de Janeiro', false),
                                                                   (35, 12, 'São Paulo', false),
                                                                   (36, 12, 'Brasília', true);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (37, 13, 'Hyper Transfer Markup Language', false),
                                                                   (38, 13, 'HyperText Markup Language', true),
                                                                   (39, 13, 'High Tech Machine Learning', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (40, 14, 'Python', true),
                                                                   (41, 14, 'Java', false),
                                                                   (42, 14, 'C++', false);

INSERT INTO options (id, question_id, option_text, is_correct) VALUES
                                                                   (43, 15, 'To repeat a block of code multiple times', true),
                                                                   (44, 15, 'To store multiple values', false),
                                                                   (45, 15, 'To define a function', false);


INSERT INTO quiz_results (id, user_id, quiz_id, score) VALUES
                                                           (1, 1, 1, 20),
                                                           (2, 2, 2, 30),
                                                           (3, 3, 3, 20),
                                                           (4, 4, 4, 30),
                                                           (5, 5, 5, 20);

alter table users alter column id restart with (select max(id) + 1 from users);
