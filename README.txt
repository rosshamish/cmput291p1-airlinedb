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

# User Guide

-Run jar
-Log into database containing flight information
-Now at log in menu, can either:
  -Log into program using existing account
  -Create account which then logs you into that account
  -Exit application
-Once logged in as regular user can either:
  -Choose to book a flight
    -Enter where you are flying from and to, aswell as the date
    -Select how you want the flights to be sorted
    -Select what booking you want to make
  -List existing bookings
    -Lists bookings and lets you select one where you can then:
      -See more information about the booking
      -Delete the booking
      -Return to main menu
  -Log out of account
-Once logged in as airline agent:
  -do everything a regular user can do
  -update a scheduled flight departure time by entering the flight code and departure date
  -update a scheduled flight arrival time by entering the flight code and departure date

# Detailed design of the software

Responsibilities and interfaces of primary classes, including relationships between them
- View class, AirlineDBConsole. It's responsible for displaying output to the user. It delegates user input tasks to the CLI and delegates application tasks to the Controller.
- Controller class, AirlineDBController. It's responsible for performing application tasks, or for delegating these tasks to other classes. It delegates database tasks to the Database class.
- Database class, AirlineDB. It's responsible for connecting to, disconnecting from, and performing queries on the database.
- Command Line Interface (CLI) class, AirlineDBCommandLineInterface. It's responsible for user input. Controller delegates user input tasks to this class.
- SQL queries class, SQLQueries. It's responsible for defining and managing access to SQL query strings. 
- 

# Testing strategy

Strategy:

-Populate tables with data from assignment 2 and insert a few tickets, users, bookings and airline agents into the respective tables.
-Check if can log into existing user, as well as airline agent
-Check if can register new user and then be logged in
-Check if pre-existing bookings exist in list bookings
-Check more detail on the bookings
-Delete all the bookings
-Try to list bookings when it's empty
-Make booking by selecting a flight from city to city
-Make booking by selecting a flight from airport code to airport code
-List bookings, make sure both exist
-Change the arrival time of flight and make sure its changed
-Change the departure time of flight and make sure its changed
-Log out of user and make sure time gets set
-Exit application

Coverage:

Test cases cover required usages of the application. Test cases assume user is not being malicious or entering improper
data/suggestions (Does not cover user not following required inputs such as dates or numbers, or user sql injecting)

Number of Bugs: 9
Nature of Bug:
-Program crashes when signing up pre-existing user
-Program crashes when logging out from failed sql query (user data was lost before query performed)
-Program crashes when trying to log in as non-existant user
-Program doesn't print column names and values together
-Program crashes when selecting flight
-Program crashes when signing in to database
-Program hangs when signing in to database
-Program can't load oracles driver (can't find .jar or .zip in classpath)
-Program crashes when typing values that arn't the prompted values

# Group work break-down strategy

Work item breakdown
- system design and construction of the UI skeleton was treated as one work item
- application functionality was broken down into user stories. Each user story was treated as one work item. Abbreviated user stories are as follows.
  1. as a new user, I can register a username and password combination with the system
  2. as an existing user, I can log in using a username and password combination in the system
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
- Andrew Bradshaw (abradsha): 16 hrs
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

-When showing more details on a booking, show all information in bookings and ticket tables (doesn't show airports, flight info etc.)
-Functionality for user story 10 and 11 does not exist
-Can only search by airport code and not by airport name
