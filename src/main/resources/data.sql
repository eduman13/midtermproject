insert into user(dtype, username, password, birthday, mailing_address, name, primary_address) values
('AccountHolder', 'eddy', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2000-12-20', 'C/ Falsa 123', 'Eddy', 'C/ Primary_Address'),
('AccountHolder', 'eddas', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '1985-12-20', 'C/ Falsa 123', 'EddyFloyd', 'C/ Primary_Address'),
('AccountHolder', 'eddyLover', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '2010-12-20', 'C/ Falsa 123', 'EddyLover', 'C/ Primary_Address'),
('AccountHolder', 'eddyFloyd', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '1985-12-20', 'C/ Falsa 123', 'Eddas', 'C/ Primary_Address'),
('AccountHolder', 'edu', '$2a$10$Mdsm8b2d1aPb77WMZAzfduAHlNQP/.OGJTHsbyIGY4m.Ra7Wmn9mW', '1985-12-20', 'C/ Falsa 123', 'Puyi', 'C/ Primary_Address');

insert into checking(balance, minimum_balance, penalty_fee, monthly_maintenance_fee, secret_key, status, money_type, primary_owner_id, secondary_owner_id) values
(275, 250, 40, 12, 'Pepe', 'ACTIVE', 'USD', 1, null),
(1040, null, 40, null, 'Oscar', 'ACTIVE', 'USD', 2, null);

insert into student_checking(student_checking_id) values
(2);

insert into user(dtype, username, password) values
('Admin', 'admin', '$2a$10$AvOmk/3b1lKyCwgV04iM2ebzRHiLScpLOujHKn4nK3evDVlQ8e7.G');

insert into role(role, user_id) values
('ROLE_ADMIN', 1),
('ROLE_ACCOUNT_HOLDER', 2),
('ROLE_ACCOUNT_HOLDER', 3),
('ROLE_ACCOUNT_HOLDER', 4),
('ROLE_ACCOUNT_HOLDER', 5),
('ROLE_ACCOUNT_HOLDER', 6);

