package com.cmput291.rhanders_abradsha_dshin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ross on 15-10-23.
 */
public class AirlineDBCommandLineInterface {
    private static final HashMap<PromptName, String[]> prompts = new HashMap<PromptName, String[]>() {{
        put(PromptName.Login, new String[]{"Login", "Register", "Exit"});
        put(PromptName.Main, new String[]{"Search for Flight", "List Bookings", "Logout"});
        put(PromptName.AgentMain, new String[]{"Search for Flight", "List Bookings",
                "Record Flight Departure", "Record Flight Arrival", "Logout"});
        put(PromptName.Bookings, new String[]{"See Booking Details", "Delete Booking", "Do Nothing"});
        put(PromptName.Connections, new String[]{"Sort by Price", "Sort by Price and Connections"});
    }};

    public Integer promptForChoice(PromptName name) {
        Integer choice = null;
        if (!prompts.containsKey(name)) {
            if (Debugging.isEnabled()) {
                System.err.println("CLI does not contain prompt with name " + name);
            }
            return null;
        }

        Integer optNum = 1;
        for (String opt : prompts.get(name)) {
            System.out.printf("%d) %s\n", optNum++, opt);
        }

        choice = Integer.valueOf(this.readLine(">> ")) - 1;
        if (choice < 0 || choice > prompts.get(name).length) {
            if (Debugging.isEnabled()) {
                System.err.println("Invalid option chosen");
            }
        }

        return choice;
    }

    public Object pickObjectFromList(ArrayList<?> objects, String clarification)
            throws InvalidChoiceException, NoChoiceException {
        Integer choice = null;
        Integer optNum = 1;
        for (Object opt : objects) {
            System.out.printf("%03d) %s\n", optNum++, opt.toString());
        }

        choice = Integer.valueOf(this.readLine(clarification + ">> ")) - 1;
        if (choice == -1) {
            throw new NoChoiceException();
        }
        if (choice < 0 || choice > objects.size()) {
            throw new InvalidChoiceException();
        }

        return objects.get(choice);
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
        search.setDepdate(this.readLine("Departure Date (DD-Mon-YYYY): "));

        return search;
    }

    public PassengerDetails inputPassengerDetails(String title) {
        PassengerDetails p = new PassengerDetails();
        if (title.length() > 0) {
            System.out.println(title);
        }

        p.name = this.readLine("Name: ");
        p.country = this.readLine("Country: ");

        return p;
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

    public ScheduledFlight inputFlightArrival() {
        String flightNo = this.readLine("Flight number: ");
        String depDate = this.readLine("Departure date (DD-Mon-YYYY): ");
        String time = this.readLine("Arrival time (hh24:mi): ");
        return new ScheduledFlight(flightNo, depDate, time, null);
    }

    public ScheduledFlight inputFlightDeparture() {
        String flightNo = this.readLine("Flight number: ");
        String depDate = this.readLine("Departure date (DD-Mon-YYYY): ");
        String time = this.readLine("Departure time (hh24:mi): ");
        return new ScheduledFlight(flightNo, depDate, null, time);
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
            if (Debugging.isEnabled()) {
                System.err.printf("readLine IOException: %s", e.getMessage());
            }
        }
        return "NULL";
    }

    private char[] readPassword(String format, Object... args) {
        if (System.console() != null)
            return System.console().readPassword(format, args);
        return this.readLine(format, args).toCharArray();
    }

    public enum PromptName {
        Login,
        Main,
        AgentMain,
        Bookings,
        Connections
    }
}
