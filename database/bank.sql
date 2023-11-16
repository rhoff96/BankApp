DROP TABLE IF EXISTS log;
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
	withdrawal_count int DEFAULT 0,
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


INSERT INTO customer(name, password) VALUES ('Russell Hoffman','adminpassword');

INSERT INTO account(customer_id, type) VALUES (1, 'Checking');
INSERT INTO account(customer_id, type) VALUES (1, 'Savings');

INSERT INTO transaction(time, account_number, previous_balance, amount) VALUES ('2023-10-22 17:55:00',1,0,100);
INSERT INTO transaction(time, account_number, previous_balance, amount) VALUES ('2023-10-22 18:16:00',2,0,200);

INSERT INTO log(time, action) VALUES ('2023-01-01 00:00:00', 'interest');
INSERT INTO log(time, action) VALUES ('2023-01-01 00:00:01', 'fee');
