cmput291p1-airlinedb

CLI for a faux airline database

> Authors: Daniel Shin, Andrew Bradshaw, Ross Anderson

Build Instructions:

// TODO build instructions

Run Instructions:

// TODO run instructions

Attribution:

Read file into String in Java
- http://stackoverflow.com/a/326440/1817465

Enum of Strings in Java
- http://stackoverflow.com/a/3978690/1817465

---

# Overview of the system

High-level intro
Faux airline database exposed as a command-line application

// TODO diagram showing data-flow between components

// TODO Small user guide

# Detailed design of the software

Responsibilities and interfaces of primary classes, including relationships between them
- View class, AirlineDBConsole. It's responsible for displaying output to the user. It delegates user input tasks to the CLI and delegates application tasks to the Controller.
- Controller class, AirlineDBController. It's responsible for performing application tasks, or for delegating these tasks to other classes. It delegates database tasks to the Database class.
- Database class, AirlineDB. It's responsible for connecting to, disconnecting from, and performing queries on the database.
- Command Line Interface (CLI) class, AirlineDBCommandLineInterface. It's responsible for user input. Controller delegates user input tasks to this class.
- SQL queries class, SQLQueries. It's responsible for defining and managing access to SQL query strings. 
- 

# Testing strategy

// TODO scenarios being tested
// TODO test case coverage
// TODO number of bugs found during testing
// TODO nature of bugs found during testing

# Group work break-down strategy

Work item breakdown
- system design and construction of the UI skeleton was treated as one work item
- application functionality was broken down into user stories. Each user story was treated as one work item. Abbreviated user stories are as follows.
  1. as a new user, I can register a username and password combination with the system
  2. as a new user, I can register a username and password combination with the system
  3. as a user, I can search for flights by source, dest, and departure date
  4. as a user, after searching, I can book a flight 
  5. as a user, I can list all my existing bookings
  6. as a user, after listing my bookings, I can cancel a booking
  7. as a user, I can logout
  8. as an airline agent, I can record a flight departure
  9. as an airline agent, I can record a flight arrival
  10. as a user, I can search and book round-trip flights
  11. as a user, I can search and book 2-connection flights
- design document was treated as one work item

Estimate of time spent
- Ross Anderson (rhanders): TODO hrs
- Andrew Bradshaw (abradsha): TODO hrs
- Daniel Shin (dshin): TODO hrs

Functionality implemented by each member
- Ross Anderson (rhanders)
  - system design and construction of the UI skeleton (75%)
  - design document
- Andrew Bradshaw (abradsha)
  - system design and construction of the UI skeleton (25%)
  - user stories 5, 6, 7, 8, 9
- Daniel Shin (dshin)
  - user stories 1, 2, 3, 4

Method of coordination for staying on track
- Facebook messenger group thread for communication
- Github repository for sharing the code

# Any assumptions or design decision extraneous to the specification on eClass

// TODO list assumptions and design decisions

