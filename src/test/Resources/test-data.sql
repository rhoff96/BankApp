START TRANSACTION;

DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
	customer_id SERIAL PRIMARY KEY,
	name varchar(50) NOT NULL,
	password varchar(50) UNIQUE NOT NULL,
	last_login timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
	account_number SERIAL PRIMARY KEY,
	customer_id int NOT NULL,
	type varchar(10) NOT NULL,
	withdrawal_count int,
	CONSTRAINT FK_customer_account FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE transaction (
	transaction_id SERIAL PRIMARY KEY,
	time timestamp NOT NULL,
	account_number int NOT NULL,
	previous_balance numeric NOT NULL,
	amount numeric NOT NULL,
	CONSTRAINT FK_transaction_account FOREIGN KEY (account_number) REFERENCES account(account_number)
);

CREATE TABLE log (
    entry_id SERIAL PRIMARY KEY,
    time timestamp NOT NULL,
    action varchar(10) NOT NULL
);


INSERT INTO customer(name, password) VALUES ('Russell Hoffman','admin'); --customer #1
INSERT INTO customer(name, password) VALUES ('Test Customer','testpassword'); -- customer #2

INSERT INTO account(customer_id, type) VALUES (1, 'Checking');--account # 1
INSERT INTO account(customer_id, type) VALUES (1, 'Savings'); -- account #2
INSERT INTO account(customer_id, type) VALUES (2, 'Checking'); -- account #3
INSERT INTO account(customer_id, type) VALUES (2, 'Savings'); -- account #4
INSERT INTO account(customer_id, type) VALUES (1, 'Checking'); -- account #5

INSERT INTO transaction(time, account_number, previous_balance, amount) VALUES ('2012-10-25 00:00:00',1,100.0,10.0);
INSERT INTO transaction(time, account_number, previous_balance, amount) VALUES ('2015-11-20 00:00:00',2,50.0,700.0);
INSERT INTO transaction(time, account_number, previous_balance, amount) VALUES ('2019-09-15 00:00:00',3,300.0,50.0);
INSERT INTO transaction(time, account_number, previous_balance, amount) VALUES ('2023-01-01 00:00:00',4,50.0, 200.0);


INSERT INTO log(time, action) VALUES ('2023-01-01 00:00:00','interest');
COMMIT;