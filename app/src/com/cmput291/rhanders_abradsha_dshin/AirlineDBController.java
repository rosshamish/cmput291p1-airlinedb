package com.cmput291.rhanders_abradsha_dshin;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.ArrayList;

/**
 * Created by ross on 15-10-23.
 */
public class AirlineDBController {
    private AirlineDB airlineDB;
    private UserDetails currentUser;

    public void connect(Credentials dbCreds) throws SQLInvalidAuthorizationSpecException {
        try {
            airlineDB = new AirlineDB(dbCreds);
        } catch (SQLInvalidAuthorizationSpecException e) {
            System.err.println("AirlineDBController failed to connect to database");
            throw e;
        }
    }

    public void disconnect() {
        airlineDB.disconnect();
    }

    public boolean logout() {
        airlineDB.executeUpdate(SQLQueries.lastLoginUpdate(currentUser.getEmail()));
        currentUser = null;
        return true;
    }

    public boolean isUserLoggedIn(UserDetails details) {
        String email = details.getEmail();
        String pass = details.getPass();
        ResultSet results = airlineDB.executeQuery(SQLQueries.login(email, pass));
        try {
            if (!results.next()) {
                return false;
            }
        } catch (SQLException e) {}                   //TODO: not sure if this is alright
        return true;
    }

    public void login(UserDetails details) {
        currentUser = details;
    }

    public Boolean isAgent() {
        return currentUser.getAgent();
    }

    public void register(UserDetails newUserDetails) {
        String email = newUserDetails.getEmail();
        String pass = newUserDetails.getPass();
        airlineDB.executeUpdate(SQLQueries.userUpdate(email, pass));
    }

    public ArrayList<ScheduledFlight> recordArrival() {
        ArrayList<ScheduledFlight> flights = new ArrayList();
        ResultSet results = airlineDB.executeQuery(SQLQueries.allScheduledFlights());
        try {
            while(results.next()) {
                flights.add(new ScheduledFlight(results));
            }
        } catch (SQLException e) {}
        return flights;
    }

    public void recordDeparture() {
        throw new NotImplementedException();
    }
}
