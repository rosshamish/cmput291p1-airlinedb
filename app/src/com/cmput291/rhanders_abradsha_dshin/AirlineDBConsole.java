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

        System.out.println("Thanks for choosing AirlineDB");
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

                if (controller.isUserLoggedIn(details)) {
                    wasLoginSuccessful = true;
                    controller.login(details);
                } else {
                    System.out.println("Invalid credentials");
                }
                break;
            case 1: // Register
                UserDetails newUserDetails = cli.inputUserDetails("Create your AirlineDB account");
                controller.register(newUserDetails);

                if (controller.isUserLoggedIn(newUserDetails)) {
                    wasLoginSuccessful = true;
                    controller.login(newUserDetails);
                } else {
                    System.out.println("Registration failed");
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
        makeBooking(search);
    }

    private void makeBooking(UserSearch search) {
        System.out.println(SearchResults.rowDes());
        SearchResults bookflight = (SearchResults)cli.printObjectRows(controller.listflights(search), "Select flight to book");

        String name = cli.inputname("Please enter your name");
        controller.updatebookings(name, bookflight);
    }

    private void listBookings() {
        ArrayList<SimpleBooking> bookings = controller.listBookings();
        if (bookings.size() == 0) {
            System.out.println("You have no bookings.");
            return;
        }
        System.out.println(SimpleBooking.rowDescription());
        SimpleBooking booking = (SimpleBooking)cli.printObjectRows(bookings, "Select booking to modify");
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
