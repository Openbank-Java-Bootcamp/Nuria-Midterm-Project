# Midterm Project

## Description of the project

This REST API represents a banking system. It can create new users, that can have the role of admins, account holders or third party users. Each role has a different level of authority to perform various tasks.
The admin can create, update, see and delete every user and account, and add a role to a user. The account holder only can see and modify the balance and transfer money from one of the accounts. And the third party can receive and send money.

It can also create different types of accounts, like Checking accounts, savings accounts, credit cards accounts and student checking account.
The students checking accounts is created when the account holder is less than 24 years old. The system deduct a specific penalty fee if the balance is below the minimum and add the interest rate after a year or a month, dependint the type of account.

To execute this functionalities it is necesary to know the routes that will be shown below.

## Technologies Used

This project was created with Java, Spring Boot and Spring Data JPA to include the SQL queries. The API was tested with Postman and with JUnit.

![Database](./images/db.png)

## Models
This API has ten models, five for the accounts and five for the users:

Account
* Account (Abstract)
* Checking (extends account)
* Credit Card (extends account)
* Savings (extends account)
* Student checking

User
* User (abstract)
* Account holder (extends user)
* Admin (extends user)
* Third party
* Role

For every model it was created the respective repository, service interface, service, controller interface and controller. And for some of the classes a DTO was created to make it easier for the user to create new accounts or users.

![Class diagram](./images/class_diagram.jpg)

## Server routes table(Method, Route or URL, Description as columns)

## Resources
Link to the trello board: https://trello.com/invite/b/ECshhmrn/13454943e678d9b4ae9df7ab2e1d2168/midterm-project

A project by Núria Mafé
