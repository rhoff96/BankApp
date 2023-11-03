# Welcome to my Banking App!

This program is designed to simulate the operations of a typical banking app. Its features include authenticating users, adding multiple accounts and account types per user, and providing withdrawal, deposit, and transfer functionality.
Additionally, it mimics other typical banking policies such as overdraft fees on transactions that fall below a minimum required balance, a limit on the number of withdrawals per session from a savings account, and savings account interest paid monthly.

I really enjoyed the process of building functionality onto a project that essentially started as a simple calculator. It was really exciting to see this program start to resemble a more sophisticated banking application over a relatively short time.


I began development of the project in Week 1 of the Tech Elevator Software Development bootcamp, and added more functionality as the program progressed. Some of these evolving features included:

-Creating Collections to hold user data

-Employing BigDecimal to make accurate financial calculations

-Using the pillars of OOP to organize the program into classes

-Using inheritance to represent savings and checking accounts

-Method Overriding to provide unique functionality for savings accounts, printing string representations of objects, and evaluating object equality

-Designing program workflow using dependency injections

-Maintaining separation of concerns

-Developing Unit Tests to improve the revision and refactoring process

-Validating user input

-Handling exceptions

-Using File I/O to store transactions in a log

In addition to the concepts covered during these first weeks, I did additional research on regular expressions in order to more easily validate user input, as well as switch statements, and enums.


Following the first few weeks of the program, I evolved my program to persist data using a PostgreSQL database.

New features include:

-Tiers based on total customer balance across all accounts

-Monthly savings account interest accrued for savings accounts, depending on customer tier

-Monthly maintenance fee debited for insufficient balance

I also added integration tests to ensure proper database transactions


Please note that a few lines of sample user data are hard coded into the current program in order to test the functionality that depends on persistent data.

I intend to build this program out with a frontend, and I hope to incorporate transfers between users as well.
