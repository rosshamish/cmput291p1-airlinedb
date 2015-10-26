package com.cmput291.rhanders_abradsha_dshin;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.ArrayList;

/**
 * Created by ross on 15-10-21.
 */
public class AirlineDBConsole {
    private AirlineDBController controller;
    private AirlineDBCommandLineInterface cli;

    private Boolean isLoggedIn = false;
    private Boolean userWantsExit = false;

    public AirlineDBConsole() {
        controller = new AirlineDBController();
        cli = new AirlineDBCommandLineInterface();
    }

    public void run() {
        System.out.println("Welcome to AirlineDB\n");

        try {
            connect();
        } catch (SQLInvalidAuthorizationSpecException e) {
            System.err.println("AirlineDBConsole failed to connect to database");
            System.exit(0);
        }
        while (!userWantsExit) {
            // Login
            while (!isLoggedIn && !userWantsExit) {
                isLoggedIn = login();
            }
            if (userWantsExit) {
                break;
            }

            // Pick action
            mainMenu();
            if (userWantsExit) {
                break;
            }
        }
        disconnect();

        System.out.println("\nThanks for choosing AirlineDB\n");
    }

    private void disconnect() {
        controller.disconnect();
    }

    private void connect() throws SQLInvalidAuthorizationSpecException {
        Credentials dbCreds = cli.inputCredentials("Please enter your Oracle database credentials");
        try {
            controller.connect(dbCreds);
            System.out.println("...connected");
        } catch (SQLInvalidAuthorizationSpecException e) {
            throw e;
        }
    }

    /*
     * @return True is user is logged in, False otherwise
     */
    private Boolean login() {
        Boolean wasLoginSuccessful = false;

        Integer choice = cli.promptForChoice(AirlineDBCommandLineInterface.PromptName.Login);
        switch (choice) {
            case 0: // Login
                UserDetails details = cli.inputUserDetails("Please enter your AirlineDB credentials");

                if (controller.userExists(details)) {
                    controller.login(details);
                    wasLoginSuccessful = true;
                } else {
                    System.out.println("Login failed: Invalid credentials");
                    wasLoginSuccessful = false;
                }
                break;
            case 1: // Register
                UserDetails newUserDetails = cli.inputUserDetails("Create your AirlineDB account");
                if (controller.userExists(newUserDetails)) {
                    wasLoginSuccessful = false;
                    System.out.println("Registration failed: User already exists");
                } else {
                    controller.register(newUserDetails);
                    controller.login(newUserDetails);
                    wasLoginSuccessful = true;
                }
                break;
            case 2: // Exit
                userWantsExit = true;
                break;
        }
        return wasLoginSuccessful;
    }

    private void mainMenu() {
        if (controller.isAgent()) {
            Integer choice = cli.promptForChoice(AirlineDBCommandLineInterface.PromptName.AgentMain);
            switch (choice) {
                case 0: // Search
                    searchForFlights();
                    break;
                case 1: // List Bookings
                    listBookings();
                    break;
                case 2: // Record Departure
                    recordDeparture();
                    break;
                case 3: // Record Arrival
                    recordArrival();
                    break;
                case 4: // Logout
                    logout();
                    break;
            }
        } else {
            Integer choice = cli.promptForChoice(AirlineDBCommandLineInterface.PromptName.Main);
            switch (choice) {
                case 0: // Search
                    searchForFlights();
                    break;
                case 1: // List Bookings
                    listBookings();
                    break;
                case 2: // Logout
                    logout();
                    break;
            }
        }
    }

    private void recordDeparture() {
        if (controller.recordDeparture(cli.inputFlightDeparture())) {
            System.out.println("Updated departure date.");
        } else {
            System.out.println("Invalid data was entered.");
        }
    }

    private void recordArrival() {
        if (controller.recordArrival(cli.inputFlightArrival())) {
            System.out.println("Updated arrival date.");
        } else {
            System.out.println("Invalid data was entered.");
        }
    }

    private void searchForFlights() {
        UserSearch search = cli.inputsearch("Please enter search criteria");
        Boolean con = false;
        Integer choice = cli.promptForChoice((AirlineDBCommandLineInterface.PromptName.Connections));
        switch (choice){
            case 0: // Search by price only
                makeBooking(search, con);
                break;
            case 1: //search by price and connections
                con = true;
                makeBooking(search, con);
                break;
        }

    }

    private void makeBooking(UserSearch search, Boolean con) {
        System.out.println(SearchResults.rowDes());
        SearchResults flightToBook = (SearchResults)cli.pickObjectFromList(controller.listFlights(search, con), "Select flight to book");

        String name = cli.inputname("Please enter your name");
        BookingStatus bookingStatus = controller.attemptBookFlight(name, flightToBook);
        switch (bookingStatus.getState()) {
            case FAIL_NO_SEATS:
                System.out.println("Booking failed: No seats left");
                break;
            case FAIL_NO_REASON:
                // TODO catch specific sqlexceptions for more specific failure messages
                // In attemptBookFlight, check what the SQLException was and add
                // BookingStatus.State enum values to reflect them. Return those BookingStatus's
                // and add more case statements here to print out a descriptive message for each
                System.out.println("Booking failed: Reason unknown");
                break;
            case SUCCESS:
                System.out.println("Booking made. Ticket Number: " +
                        String.valueOf(bookingStatus.getTicketNo()));
                break;
        }
    }

    private void listBookings() {
        ArrayList<SimpleBooking> bookings = controller.listBookings();
        if (bookings.size() == 0) {
            System.out.println("You have no bookings.");
            return;
        }
        System.out.println(SimpleBooking.rowDescription());
        SimpleBooking booking = (SimpleBooking)cli.pickObjectFromList(bookings, "Select booking to modify");
        while(true) {
            Integer choice = cli.promptForChoice(AirlineDBCommandLineInterface.PromptName.Bookings);
            switch (choice) {
                case 0: // Booking Details
                    System.out.println(booking.descriptiveToString());
                    break;
                case 1: // Delete Booking
                    controller.deleteBooking(booking);
                    System.out.println("Deleted.");
                    return;
                case 2: // Return
                    return;
            }
        }
    }

    private void logout() {
        isLoggedIn = !controller.logout();
        System.out.println("Logged out.");
    }
}
