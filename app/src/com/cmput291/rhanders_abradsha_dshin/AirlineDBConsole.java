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
    private final String[] loginOptions = {"Login", "Register"};

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
        String choice = loginOptions[getChoice(loginOptions)];
        switch (choice) {
            case "Login":
                // TODO log an existing user in
                break;
            case "Register":
                // TODO register a new user
                break;
        }
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
