INSERT INTO roles VALUES (1, 'admin');
INSERT INTO roles VALUES (2, 'agent');
INSERT INTO roles VALUES (3, 'vendor');

INSERT INTO user_details(id, username, age, first_name, last_name) VALUES (1, 'admin@mock.com', 36, 'Mock-Admin', 'Mock-Last');
INSERT INTO user_details(id, username, age, first_name, last_name) VALUES (2, 'employee@mock.com', 24, 'Mock-employee', 'Mock-employee-Last');
INSERT INTO user_details(id, username, age, first_name, last_name) VALUES (3, 'vendor@mock.com', 24, 'Mock-vendor', 'Mock-vendor-Last');

INSERT INTO users(id, username, password, user_dtl_id) VALUES (1 , 'admin@mock.com','$2a$10$.Rxx4JnuX8OGJTIOCXn76euuB3dIGHHrkX9tswYt9ECKjAGyms30W', 1);
INSERT INTO users(id, username, password, user_dtl_id) VALUES (2, 'employee@mock.com','$2a$10$.Rxx4JnuX8OGJTIOCXn76euuB3dIGHHrkX9tswYt9ECKjAGyms30W', 2);
INSERT INTO users(id, username, password, user_dtl_id) VALUES (3, 'vendor@mock.com','$2a$10$.Rxx4JnuX8OGJTIOCXn76euuB3dIGHHrkX9tswYt9ECKjAGyms30W', 3);

INSERT INTO USER_ROLES VALUES (1, 1);
INSERT INTO USER_ROLES VALUES (2, 2);
INSERT INTO USER_ROLES VALUES (3, 3);

