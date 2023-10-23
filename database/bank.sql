DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
	customer_id SERIAL PRIMARY KEY,
	name varchar(50) NOT NULL,
	password varchar(50) UNIQUE
);

CREATE TABLE account (
	account_number SERIAL PRIMARY KEY,
	customer_id int NOT NULL,
	type varchar(10) NOT NULL,
	CONSTRAINT FK_customer_account FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE transaction (
	transaction_id SERIAL PRIMARY KEY,
	time timestamp NOT NULL,
	customer_id int NOT NULL,
	account_number int NOT NULL,
	previous_balance money NOT NULL,
	amount money NOT NULL,
	CONSTRAINT FK_transaction_customer FOREIGN KEY (customer_id ) REFERENCES customer(customer_id),
	CONSTRAINT FK_transaction_account FOREIGN KEY (account_number) REFERENCES account(account_number)
);

INSERT INTO customer(name, password) VALUES ('Russell Hoffman','admin');

INSERT INTO account(customer_id, type) VALUES (1, 'Checking');
INSERT INTO account(customer_id, type) VALUES (1, 'Savings');

INSERT INTO transaction(time, customer_id, account_number, previous_balance, amount) VALUES ('2023-10-22 17:55:00',1,1,0,100);
INSERT INTO transaction(time, customer_id, account_number, previous_balance, amount) VALUES ('2023-10-22 18:16:00',1,2,0,200);
