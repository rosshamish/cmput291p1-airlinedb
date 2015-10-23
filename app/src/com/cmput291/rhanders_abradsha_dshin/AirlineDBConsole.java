package com.cmput291.rhanders_abradsha_dshin;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ross on 15-10-21.
 */
public class AirlineDBConsole {
    private AirlineDBController controller;
    private final String[] loginOptions = {"Login", "Register"};
    private Boolean userWantsExit = false;

    private Boolean tryingToLogin = false;
    private Boolean loggedIn = false;

    public void run() {
        System.out.println("Welcome to AirlineDB\n");

        connect();
        while (!userWantsExit) {
            // Login
            tryingToLogin = true;
            while (tryingToLogin) {
                loggedIn = login();
                if (!loggedIn) {
                    tryingToLogin = askQuestion("Re-try login?");
                }
            }
            if (!loggedIn) {
                break;
            }

            // Pick action
            
        }
        System.out.println("Bye!");
        disconnect();
    }

    private void disconnect() {
        controller.disconnect();
    }

    private void connect() {
        Credentials dbCreds = getCredentials("Please enter your Oracle database credentials");
        controller.connect(dbCreds);
        System.out.println("...connected");
    }

    /*
     * @return True is user is logged in, False otherwise
     */
    private Boolean login() {
        String choice = loginOptions[getChoice(loginOptions)];
        Boolean wasLoginSuccessful = false;
        switch (choice) {
            case "Login":
                Credentials loginCreds = getCredentials("Please enter your airline credentials");
                controller.login(loginCreds);
                if (controller.isUserLoggedIn(loginCreds)) {
                    wasLoginSuccessful = true;
                } else {
                    System.out.println("Invalid credentials");
                }
                break;
            case "Register":
                UserDetails newUserDetails = getUserDetails("Create your airline account");
                controller.register(newUserDetails);
                controller.login(newUserDetails.getCreds());
                if (controller.isUserLoggedIn(newUserDetails.getCreds())) {
                    wasLoginSuccessful = true;
                } else {
                    System.out.println("Registration failed");
                }
                break;
        }
        return wasLoginSuccessful;
    }

    private UserDetails getUserDetails(String title) {
        UserDetails details = new UserDetails();

        details.setCreds(getCredentials(title));
        details.setName(readLine("Name: "));
        details.setEmail(readLine("Email: "));

        return details;
    }

    // @return index in options of the user's choice
    private Integer getChoice(String[] options) {
        Integer choice = null;

        Integer optNum = 1;
        for (String opt : options) {
            System.out.printf("%d) %s\n", optNum++, opt);
        }

        choice = Integer.valueOf(this.readLine("Selection>> ")) - 1;
        if (choice < 0 || choice > options.length) {
            System.err.println("Invalid option chosen");
        }

        return choice;
    }

    private Boolean askQuestion(String question) {
        String ans = readLine(question + " (y/n) [y]");
        return (ans == "") || (ans == "y") || (ans == "Y");
    }

    private Credentials getCredentials(String question) {
        Credentials creds = new Credentials();
        if (question.length() > 0) {
            System.out.println(question);
        }
        creds.setUser(this.readLine("Username: "));
        creds.setPass(this.readPassword("Password: "));
        return creds;
    }

    private CredentialsDetail getCredentialsDetail

    private String readLine(String format, Object... args) {
        if (System.console() != null) {
            return System.console().readLine(format, args);
        }
        System.out.print(String.format(format, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.err.printf("readLine IOException: %s", e.getMessage());
        }
        return "NULL";
    }

    private char[] readPassword(String format, Object... args) {
        if (System.console() != null)
            return System.console().readPassword(format, args);
        return this.readLine(format, args).toCharArray();
    }
}
