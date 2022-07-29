INSERT INTO categories (title, color) VALUES ('LIVRE', 'WHITE');
INSERT INTO roles (value) VALUES ('ADMIN');
INSERT INTO roles (value) VALUES ('CLIENT');

INSERT INTO users (name, email, secret) VALUES
    ('Julio', 'julio@test.com', '$2a$10$ofEy..aODV5QleKty0kkJ.8UXdOXIdr/CeyXswcjJGBVYgxU296NK');
INSERT INTO users (name, email, secret) VALUES
    ('Cauan', 'cauan@test.com', '$2a$10$ofEy..aODV5QleKty0kkJ.8UXdOXIdr/CeyXswcjJGBVYgxU296NK');

INSERT INTO users_roles (user_entity_id, roles_id) VALUES (1, 1);
INSERT INTO users_roles (user_entity_id, roles_id) VALUES (1, 2);
INSERT INTO users_roles (user_entity_id, roles_id) VALUES (2, 2);
