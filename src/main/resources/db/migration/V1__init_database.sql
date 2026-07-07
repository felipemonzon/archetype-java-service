-- archetype.enterprises_seq definition

CREATE TABLE IF NOT EXISTS `enterprises_seq` (
    `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO enterprises_seq (next_val)
VALUES(1);

-- archetype.enterprises definition

CREATE TABLE IF NOT EXISTS `enterprises` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `id_enterprise` VARCHAR(20) NOT NULL,
    `rfc` VARCHAR(20) NOT NULL,
    `status` VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    `address` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `phone` varchar(10) COLLATE utf8mb4_general_ci NOT NULL,
    `manager` varchar(100) NOT NULL,
    `created_user` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `created_date` datetime(6) NOT NULL,
    `last_modified_user` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `last_modified_date` datetime(6) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO enterprises
(id_enterprise, rfc, status, address, name, phone, manager, created_user, created_date, last_modified_user, last_modified_date)
VALUES('ENT6734htdge362xnhd3', 'JUJI877654HSFH12', 'ACTIVE', 'muy lejos', 'empresa', '1234567898', 'Felipe Monzon', 'ADMIN', NOW(), 'ADMIN', NOW());

-- archetype.roles_seq definition

CREATE TABLE IF NOT EXISTS `roles_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO roles_seq (next_val)
VALUES(1);

-- archetype.roles definition

CREATE TABLE IF NOT EXISTS roles (
	id BIGINT auto_increment NOT NULL,
	name varchar(100) NOT NULL,
	value varchar(100) NOT NULL,
	status tinyint(1) NULL DEFAULT 0,
	created_user varchar(250) NOT NULL,
	created_date datetime NOT NULL,
	last_modified_user varchar(255) NOT NULL,
	last_modified_date datetime NOT NULL,
	CONSTRAINT role_PK PRIMARY KEY (id),
	CONSTRAINT role_UN UNIQUE KEY (name,value)
);

INSERT INTO roles (name, value, status, created_user, created_date, last_modified_user, last_modified_date)
VALUES ('ROLE_ADMIN', 'ADMIN', 1, 'ADMIN', NOW(), 'ADMIN', NOW()),
('ROLE_CUSTOMER', 'Cliente', 1, 'ADMIN', NOW(), 'ADMIN', NOW());

-- archetype.users_seq definition

CREATE TABLE IF NOT EXISTS `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO users_seq (next_val)
VALUES(1);

-- archetype.users definition

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint auto_increment NOT NULL,
  `id_user` VARCHAR(20) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(200) NOT NULL,
  `username` varchar(20) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `genre` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT 'ACTIVE',
  `id_enterprise` bigint NOT NULL,
  `created_user` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_user` varchar(255) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO users
(id_user, created_user, created_date, last_modified_user, last_modified_date, phone, email, first_name, genre, last_name, password, username, status, id_enterprise)
VALUES('USU324htgd243yt567jh', 'ADMIN', NOW(), 'ADMIN', NOW(), '6671568899', 'felipemonzon2705@gmail.com', 'Felipe', 0, 'Monzon', '$2a$10$K9UyV7Eiwoi8Udv/9R5kROuDvz/K6ZVLJzzESW2lVe7B.FfXRg0hK', 'felipemonzon2705', 'ACTIVE', 1);

-- archetype.user_roles definition

CREATE TABLE IF NOT EXISTS `user_roles` (
  `id_user` bigint NOT NULL,
  `id_role` bigint NOT NULL,
  PRIMARY KEY (`id_user`,`id_role`),
  KEY `FK1v995xldvmr6w96c5feofx1gf` (`id_role`),
  CONSTRAINT `role_FK` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`),
  CONSTRAINT `user_role_FK` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO user_roles(id_user, id_role)
VALUES(1, 1);