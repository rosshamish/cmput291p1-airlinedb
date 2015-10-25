package com.cmput291.rhanders_abradsha_dshin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by ross on 15-10-23.
 */
public class AirlineDBCommandLineInterface {
    public enum PromptName {
        Login,
        Main,
        AgentMain
    }

    private static final HashMap<PromptName, String[]> prompts = new HashMap<PromptName, String[]>() {{
        put(PromptName.Login, new String[]{"Login", "Register", "Exit"});
        put(PromptName.Main, new String[]{"Search for Flight", "List Bookings", "Cancel Bookings", "Logout"});
        put(PromptName.AgentMain, new String[]{"Search for Flight", "List Bookings", "Cancel Bookings",
                "Record Flight Departure", "Record Flight Arrival", "Logout"});
    }};

    public Integer promptForChoice(PromptName name) {
        Integer choice = null;
        if (!prompts.containsKey(name)) {
            System.err.println("CLI does not contain prompt with name " + name);
            return null;
        }

        Integer optNum = 1;
        for (String opt : prompts.get(name)) {
            System.out.printf("%d) %s\n", optNum++, opt);
        }

        choice = Integer.valueOf(this.readLine(">> ")) - 1;
        if (choice < 0 || choice > prompts.get(name).length) {
            System.err.println("Invalid option chosen");
        }

        return choice;
    }

    public Object printObjectRows(Object[] objects) {
        Integer choice = null;
        Integer optNum = 1;
        for (Object opt : objects) {
            System.out.printf("%d) %s\n", optNum++, opt.toString());
        }

        choice = Integer.valueOf(this.readLine(">> ")) - 1;
        if (choice < 0 || choice > objects.length) {
            System.err.println("Invalid option chosen");
        }

        return objects[choice];
    }

    public UserDetails inputUserDetails(String title) {
        UserDetails details = new UserDetails();

        if (title.length() > 0) {
            System.out.println(title);
        }

        details.setEmail(this.readLine("Email: "));
        details.setPass(this.readPassword("Password: "));

        return details;
    }

    public UserSearch inputsearch(String title) {
        UserSearch search = new UserSearch();

        if (title.length() > 0) {
            System.out.println(title);
        }

        search.setSrc(this.readLine("Source Airport: "));
        search.setDst(this.readLine("Destination Airport: "));
        search.setDepdate(this.readLine("Departure Date: "));

        return search;
    }

    public Credentials inputCredentials(String title) {
        Credentials creds = new Credentials();

        if (title.length() > 0) {
            System.out.println(title);
        }
        creds.setUser(this.readLine("Username: "));
        creds.setPass(this.readPassword("Password: "));

        return creds;
    }

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
