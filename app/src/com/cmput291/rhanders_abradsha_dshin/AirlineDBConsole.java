package com.cmput291.rhanders_abradsha_dshin;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ross on 15-10-21.
 */
public class AirlineDBConsole {
    private AirlineDB airlineDB;
    private Boolean isAgent = false;
    private final String[] loginOptions = {"Login", "Register", "Exit"};
    private final String[] mainMenuOptions = {"Search for Flight", "List Bookings", "Cancel Bookings", "Logout"};
    private final String[] mainMenuOptionsAgent = {"Search for Flight", "List Bookings", "Cancel Bookings",
            "Record Flight Departure", "Record Flight Arrival", "Logout"};

    public void run() {
        connect();
        presentLoginPrompt();
    }

    private void connect() {
        System.out.println("Welcome to AirlineDB\n");
        Credentials dbCreds = getCredentials("Please enter your Oracle database credentials");
        airlineDB = new AirlineDB(dbCreds);
        System.out.println("...connected");
    }

    private void presentLoginPrompt() {
        Integer choice = getChoice(loginOptions);
        switch (choice) {
            case 0:
                login();
                break;
            case 1:
                createAccount();
                break;
            case 2:
                System.exit(0);
        }
    }

    private void presentMainMenuPrompt() {
        if (isAgent) {
            Integer choice = getChoice(mainMenuOptionsAgent);
            switch (choice) {
                case 0:
                    searchForFlights();
                    break;
                case 1:
                    listBookings();
                    break;
                case 2:
                    cancelBooking();
                    break;
                case 3:
                    recordDeparture();
                    break;
                case 4:
                    recordArrival();
                    break;
                case 5:
                    logout();
                    break;
            }
        }
        else {
            Integer choice = getChoice(mainMenuOptions);
            switch (choice) {
                case 0:
                    searchForFlights();
                    break;
                case 1:
                    listBookings();
                    break;
                case 2:
                    cancelBooking();
                    break;
                case 3:
                    logout();
                    break;
            }
        }
    }

    private void recordDeparture() {
        // TODO
        System.out.println("WOW TURNIPS SURE ARE DELICIOUS");
        presentMainMenuPrompt();
    }

    private void recordArrival() {
        // TODO
        System.out.println("MAN SURE WISH THIS SOFTWARE WORKED AND DIDNT PRINT RANDOM MESSAGES");
        presentMainMenuPrompt();
    }

    private void searchForFlights() {
        // TODO
        System.out.println("WOW THESE WERE EXCITING FLIGHTS TO CHOOSE FROM");
        presentMainMenuPrompt();
    }

    private void listBookings() {
        // TODO
        System.out.println("LOOK AT ALL THESE FANTASTIC BOOKINGS");
        presentMainMenuPrompt();
    }

    private void cancelBooking() {
        // TODO
        System.out.println("WHOOPS DELETED ALL YOUR BOOKINGS");
        presentMainMenuPrompt();
    }

    private void createAccount() {
        try {
            String username = this.readLine("Username: ");
            char[] password = this.readPassword("Password: ");
            if (canCreateAccount(username, password)) {
                System.out.println("Account created.");
                presentMainMenuPrompt();
            } else {
                System.out.println("Username and password combination already exist.");
                presentLoginPrompt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean canCreateAccount(String username, char[] password) {
        // TODO IMPLEMENT CREATING USER
        return true;
    }

    private void login() {
        try {
            String username = this.readLine("Username: ");
            char[] password = this.readPassword("Password: ");
            if (canLogin(username, password)) {
                System.out.println("Logged In.");
                presentMainMenuPrompt();
            } else {
                System.out.println("Username and password combination do not exist.");
                presentLoginPrompt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean canLogin(String username, char[] password) {
        // TODO IMPLEMENT LOGIN CHECK, SET GLOBAL TO TYPE OF USER IF AGENT OR NOT
        isAgent = true;
        return true;
    }

    private void logout() {
        // TODO ACTUALLY LOG SOMEONE OUT
        isAgent = false;
        System.out.println("Logged out.");
        presentLoginPrompt();
    }

    // @return index in options of the user's choice
    private Integer getChoice(String[] options) {
        Integer optNum = 1;
        for (String opt : options) {
            System.out.printf("%d) %s\n", optNum++, opt);
        }
        Integer choice = null;
        try {
            choice = Integer.valueOf(this.readLine("Selection>> ")) - 1;
        } catch (IOException e) {
            System.err.printf("IOException: %s\n", e.getMessage());
        }
        if (choice < 0 || choice > options.length) {
            System.err.println("Invalid option chosen");
            return -1;
        } else {
            return choice;
        }
    }

    private Credentials getCredentials(String question) {
        Credentials creds = new Credentials();
        System.out.println(question);
        try {
            creds.setUser(this.readLine("Username: "));
            creds.setPass(this.readPassword("Password: "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return creds;
    }

    private String readLine(String format, Object... args) throws IOException {
        if (System.console() != null) {
            return System.console().readLine(format, args);
        }
        System.out.print(String.format(format, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        return reader.readLine();
    }

    private char[] readPassword(String format, Object... args)
            throws IOException {
        if (System.console() != null)
            return System.console().readPassword(format, args);
        return this.readLine(format, args).toCharArray();
    }
}
