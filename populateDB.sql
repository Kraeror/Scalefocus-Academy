INSERT INTO users(created_on, email, full_name, password, phone_number, username)
VALUES(CURRENT_DATE, 'support@scalefocus.com', 'Ivan Petrov Petrov', '$2a$10$R34XaRUigSV8tIp.jEsw4ef1fVxkSk955FbQwYJismlQ5WjG1EDuW',
'+359876474829', 'ivito'),
(CURRENT_DATE, 'something@scalefocus.com', 'Georgi Iliev Ivanov', '$2a$10$R34XaRUigSV8tIp.jEsw4ef1fVxkSk955FbQwYJismlQ5WjG1EDuW',
'+359123456784', 'goshetoiliev'),
(CURRENT_DATE, 'poshtata@scalefocus.com', 'Stanislav Nikolaev Ivanov', '$2a$10$R34XaRUigSV8tIp.jEsw4ef1fVxkSk955FbQwYJismlQ5WjG1EDuW',
'0879372165', 'stanchoyy');
--The encrypted password: 12345

INSERT INTO account_types(type,monthly_fee,transaction_fee) VALUES('Checking',4,1),('Certification of deposit',0,50);

INSERT INTO checking_accounts(created_on, balance, user_id, type_id, iban)
VALUES(CURRENT_DATE, 1400.50, 1, 1, 'BG79BNPA94409332615387'),
(CURRENT_DATE, 230.30, 2, 1, 'BG98STSA93008552588915'),
(CURRENT_DATE, 560.50, 3, 1, 'BG85IORT80948136525651'),
(CURRENT_DATE, 70.70, 3, 1, 'BG85IORT80948136525777');

INSERT INTO roles(role)
VALUES('USER'),
('ADMIN');

INSERT INTO transactions(created_on, uuid, amount, reason, type, account_id)
VALUES(CURRENT_DATE, '3ccee586-3271-11ed-a261-0242ac120002', 320, 'family', 'withdraw', 1),
(CURRENT_DATE, '4c8a23dc-3271-11ed-a261-0242ac120002', 320, 'family', 'deposit', 2),
(CURRENT_DATE, '56d477c0-3271-11ed-a261-0242ac120002', 5000, '', 'deposit', 3);

INSERT INTO users_roles(user_id, role_id)
VALUES(1, 1),
(2, 1),
(3, 1),
(3, 2);

INSERT INTO loan_types(consideration_Fee,interest_rate,monthly_fee,name)
VALUES(250,2.89,5.50,'mortgage'),(250,5.25,5.50,'consumer');

INSERT INTO loans(created_on,approved, beginning_loan_amount, due_date, maturity_date, monthly_payment, period_in_months, remaining_loan_amount, total_amount_sum, account_id, type_id)
VALUES ('2022-10-01 11:33:50.759562',true,10000.00,'2032-10-01','2022-10-31',107.29,120,10000.00,13785.00,1,2);