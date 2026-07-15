INSERT INTO enterprises(id, id_enterprise, rfc, status, address, name, phone, manager, created_user, created_date, last_modified_user, last_modified_date)
VALUES(1000,'ENT6734htdge362xnhd3', 'JUJI877654HSFH12', 'ACTIVE', 'muy lejos', 'empresa', '1234567898', 'Felipe Monzon', 'ADMIN', NOW(), 'ADMIN', NOW());

INSERT INTO roles (id, name, value, status, created_user, created_date, last_modified_user, last_modified_date)
VALUES (1000,'ROLE_ADMIN-2', 'ADMIN', 1, 'ADMIN', NOW(), 'ADMIN', NOW()),
       (1001,'ROLE_CUSTOMER-2', 'Cliente', 1, 'ADMIN', NOW(), 'ADMIN', NOW());


INSERT INTO users(id, id_user, created_user, created_date, last_modified_user, last_modified_date, phone, email, first_name, genre, last_name, password, username, status, id_enterprise)
VALUES(1000,'USU324htgd243yt567jh', 'ADMIN', NOW(), 'ADMIN', NOW(), '6671223322', 'test@gmail.com', 'Test', 'MALE', 'Test', '$2a$10$K9UyV7Eiwoi8Udv/9R5kROuDvz/K6ZVLJzzESW2lVe7B.FfXRg0hK', 'test_user', 'ACTIVE', 1000);

INSERT INTO user_roles (id_user, id_role)
VALUES (1000, 1000);
