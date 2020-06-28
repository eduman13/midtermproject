insert into user(dtype, username, password, birthday, mailing_address, name, primary_address) values
('AccountHolder', 'eddy', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2000-12-20', 'C/ Falsa 123', 'Eddy', 'C/ Primary_Address'),
('AccountHolder', 'eddas', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '1985-12-20', 'C/ Falsa 123', 'EddyFloyd', 'C/ Primary_Address'),
('AccountHolder', 'eddyLover', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'EddyLover', 'C/ Primary_Address'),
('AccountHolder', 'eddyFloyd', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '1985-12-20', 'C/ Falsa 123', 'Eddas', 'C/ Primary_Address'),
('AccountHolder', 'edu', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '1985-12-20', 'C/ Falsa 123', 'Puyi', 'C/ Primary_Address'),
('AccountHolder', 'alig', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'AliG', 'C/ Primary_Address'),
('AccountHolder', 'pedrin', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'Pedrin', 'C/ Primary_Address'),
('AccountHolder', 'user8', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user8', 'C/ Primary_Address'),
('AccountHolder', 'user9', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user9', 'C/ Primary_Address'),
('AccountHolder', 'user10', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user10', 'C/ Primary_Address'),
('AccountHolder', 'user11', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user11', 'C/ Primary_Address'),
('AccountHolder', 'user12', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user12', 'C/ Primary_Address'),
('AccountHolder', 'user13', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user13', 'C/ Primary_Address'),
('AccountHolder', 'user14', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user14', 'C/ Primary_Address'),
('AccountHolder', 'user15', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user15', 'C/ Primary_Address'),
('AccountHolder', 'user16', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user16', 'C/ Primary_Address'),
('AccountHolder', 'user17', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user17', 'C/ Primary_Address'),
('AccountHolder', 'user18', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user18', 'C/ Primary_Address'),
('AccountHolder', 'user19', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'user19', 'C/ Primary_Address');

insert into checking(balance, minimum_balance, penalty_fee, monthly_maintenance_fee, secret_key, status, money_type, primary_owner_id, secondary_owner_id) values
(275, 250, 40, 12, 'Pepe', 'ACTIVE', 'USD', 1, null),
(1040, null, 40, null, 'Oscar', 'ACTIVE', 'USD', 2, null),
(560, 150, 40, 12, 'Macarena', 'FROZEN', 'USD', 3, null),
(560, 150, 40, 12, 'Lopez', 'ACTIVE', 'USD', 8, null),
(560, 150, 40, 12, 'Fito', 'ACTIVE', 'USD', 9, null),
(560, 150, 40, 12, 'Sanchez', 'ACTIVE', 'USD', 10, null),
(560, 150, 40, 12, 'Copernico', 'ACTIVE', 'USD', 11, null),
(560, 150, 40, 12, 'Eustaquia', 'ACTIVE', 'USD', 15, null),
(560, 150, 40, 12, 'Contador', 'ACTIVE', 'USD', 16, null),
(560, 150, 40, 12, 'Maquinote', 'ACTIVE', 'USD', 17, null),
(560, 150, 40, 12, 'Tronco', 'ACTIVE', 'USD', 18, null),
(560, 150, 40, 12, 'Omelette', 'ACTIVE', 'USD', 19, null);

insert into student_checking(student_checking_id) values
(2);

insert into user(dtype, username, password) values
('Admin', 'admin', '$2a$10$AvOmk/3b1lKyCwgV04iM2ebzRHiLScpLOujHKn4nK3evDVlQ8e7.G');

insert into user(dtype, username, password, hash_key) values
('ThirdParty', 'eduman', '$2a$10$wKnJ6RxPPfBkEhEvNZ604e7FMy3.xxiIkMQ1mZmwTipZ4sN0dLIvO', 'f6a55bebd2ba188ca5b7a8e921fd9a76cad5323c942eacba7d748c087ff6835a');

insert into credit_card(balance, credit_limit, interest_rate, penalty_fee, money_type, creation_date, secret_key, primary_owner_id, secondary_owner_id) values
(1000, 150, 0.1, 40, 'USD', '2020-06-25', 'Cupido', 1, null);

insert into role(role, user_id) values
('ROLE_ACCOUNT_HOLDER', 1),
('ROLE_ACCOUNT_HOLDER', 2),
('ROLE_ACCOUNT_HOLDER', 3),
('ROLE_ACCOUNT_HOLDER', 4),
('ROLE_ACCOUNT_HOLDER', 5),
('ROLE_ACCOUNT_HOLDER', 6),
('ROLE_ACCOUNT_HOLDER', 7),
('ROLE_ACCOUNT_HOLDER', 8),
('ROLE_ACCOUNT_HOLDER', 9),
('ROLE_ACCOUNT_HOLDER', 10),
('ROLE_ACCOUNT_HOLDER', 11),
('ROLE_ACCOUNT_HOLDER', 12),
('ROLE_ACCOUNT_HOLDER', 13),
('ROLE_ACCOUNT_HOLDER', 14),
('ROLE_ACCOUNT_HOLDER', 15),
('ROLE_ACCOUNT_HOLDER', 16),
('ROLE_ACCOUNT_HOLDER', 17),
('ROLE_ACCOUNT_HOLDER', 18),
('ROLE_ACCOUNT_HOLDER', 19),
('ROLE_ADMIN', 20),
('ROLE_THIRD_PARTY', 21);

