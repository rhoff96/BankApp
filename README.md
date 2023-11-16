# Banking App Overview

This program is designed to simulate the operations of a typical personal banking application.

I began development of the project in Week 1 of the Tech Elevator Software Development bootcamp, and added more functionality as the program progressed.

I really enjoyed the process of adding functionality to a project that essentially started as a simple calculator. It was really exciting to see this program start to resemble a more sophisticated banking application over a relatively short time.


# Functionality
- Authenticating users
- Creating multiple accounts and account types per user
- Providing withdrawal, deposit, and transfer functions
- Overdraft and monthly maintenance fees
- Savings account monthly withdrawal limits
- Savings account interest accrued monthly according to each user's total balance across all accounts
- Fully customizable report generation for administrators 

# Programming elements
- Creating Collections to hold user data 
- Employing BigDecimal to make accurate financial calculations 
- Using the pillars of OOP to organize the program into classes 
- Method Overriding to provide unique functionality printing string representations of objects and evaluating object equality 
- Designing program workflow using dependency injections
- Instantiating workflow classes to facilitate testing
- Maintaining separation of concerns 
- Developing Unit Tests to improve the revision and refactoring process 
- Validating user input 
- Handling exceptions 
- Using File I/O to export reports to a log file

In addition to the concepts covered during these first weeks, I did additional research on regular expressions in order to more easily validate user input, as well as switch statements and enums.

# Data Persistence
Following the first few weeks of the program, I evolved my program to persist data using a PostgreSQL database. New elements include:

- Using a PostgreSQL relational database to store user, account, and transaction data
- Using JDBC template to communicate with database
- Developing integration tests with a mock database to ensure proper database transactions
- Using TimerTask to run monthly balance methods (account fees and withdrawal counters)


# Future Functionality
I intend to build this program out with an API and a frontend, incorporate transfers between users, and implement more robust security.

