package com.cmput291.rhanders_abradsha_dshin;

import java.sql.SQLInvalidAuthorizationSpecException;

/**
 * Created by ross on 15-10-21.
 */
public class AirlineDBConsole {
    private AirlineDBController controller;
    private AirlineDBCommandLineInterface cli;

    private Boolean isAgent = false;
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
                } else {
                    System.out.println("Invalid credentials");
                }
                break;
            case 1: // Register
                UserDetails newUserDetails = cli.inputUserDetails("Create your AirlineDB account");
                controller.register(newUserDetails);

                if (controller.isUserLoggedIn(newUserDetails)) {
                    wasLoginSuccessful = true;
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
        if (isAgent) {
            Integer choice = cli.promptForChoice(AirlineDBCommandLineInterface.PromptName.AgentMain);
            switch (choice) {
                case 0: // Search
                    searchForFlights();
                    break;
                case 1: // List Bookings
                    listBookings();
                    break;
                case 2: // Cancel Booking
                    cancelBooking();
                    break;
                case 3: // Record Departure
                    recordDeparture();
                    break;
                case 4: // Record Arrival
                    recordArrival();
                    break;
                case 5: // Logout
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
                case 2: // Cancel Booking
                    cancelBooking();
                    break;
                case 3: // Logout
                    logout();
                    break;
            }
        }
    }

    private void recordDeparture() {
        // TODO
        System.out.println("WOW TURNIPS SURE ARE DELICIOUS");
        mainMenu();
    }

    private void recordArrival() {
        // TODO
        System.out.println("MAN SURE WISH THIS SOFTWARE WORKED AND DIDNT PRINT RANDOM MESSAGES");
        mainMenu();
    }

    private void searchForFlights() {
        // TODO
        System.out.println("WOW THESE WERE EXCITING FLIGHTS TO CHOOSE FROM");
        mainMenu();
    }

    private void listBookings() {
        // TODO
        System.out.println("LOOK AT ALL THESE FANTASTIC BOOKINGS");
        mainMenu();
    }

    private void cancelBooking() {
        // TODO
        System.out.println("WHOOPS DELETED ALL YOUR BOOKINGS");
        mainMenu();
    }

    private void logout() {
        isAgent = false;
        isLoggedIn = controller.logout();
        System.out.println("Logged out.");
    }
}
