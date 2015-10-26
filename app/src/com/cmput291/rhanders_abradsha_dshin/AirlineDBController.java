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
        details.setAgent(false);
        ResultSet uresults = airlineDB.executeQuery(SQLQueries.loginU(email, pass));
        ResultSet aresults = airlineDB.executeQuery(SQLQueries.loginA(email));

        try {
            if (!uresults.next()) {
                return false;
            }
            if(uresults.next() && aresults.next()){
                details.setAgent(true);            }
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

        ResultSet usrresults = airlineDB.executeQuery(SQLQueries.checkusr(email));
        try {
            if (usrresults.next()){
                System.out.println("This email has already been registered");
            }
        }catch (SQLException e){}

        airlineDB.executeUpdate(SQLQueries.userUpdate(email, pass));
    }

    public void updatebookings(String name, SearchResults results){
        airlineDB.executeUpdate(SQLQueries.bookingupdate(name, results));
    }


    public ArrayList<SearchResults> listflights(UserSearch search){
        ArrayList<SearchResults> listflights = new ArrayList();
        ResultSet results = airlineDB.executeQuery(SQLQueries.userSearchQuery(search.getSrc(),search.getDst(),search.getDepdate()));
        try{
            while(results.next()){
                listflights.add(new SearchResults(results));
            }
        }catch (SQLException e){}

        return listflights;
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

    public ArrayList<SimpleBooking> listBookings() {
        ArrayList<SimpleBooking> bookings = new ArrayList();
        ResultSet results = airlineDB.executeQuery(SQLQueries.userBookingsQuery(currentUser.getEmail()));
        try {
            while(results.next()) {
                bookings.add(new SimpleBooking(results));
            }
        } catch (SQLException e) {}
        return bookings;
    }

    public void deleteBooking(SimpleBooking booking) {
        airlineDB.executeUpdate(SQLQueries.cancelBookingUpdate(booking.getTicketNo(),
                booking.getFlightNo(), booking.getDepDate()));
    }

    public void recordDeparture() {
        throw new NotImplementedException();
    }
}
