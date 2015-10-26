package com.cmput291.rhanders_abradsha_dshin;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.ArrayList;
import java.util.Random;

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
            if(aresults.next()){
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
        newUserDetails.setAgent(false);
        ResultSet usrresults = airlineDB.executeQuery(SQLQueries.checkusr(email));
        try {
            if (usrresults.next()){
                System.out.println("This email has already been registered");
                return;
            }
        }catch (SQLException e){}

        airlineDB.executeUpdate(SQLQueries.userUpdate(email, pass));
    }

    public void updatebookings(String name, SearchResults search){
        ResultSet checkpass = airlineDB.executeQuery(SQLQueries.checkname(name));
        try{
            if(!checkpass.next()){
                ResultSet airline = airlineDB.executeQuery(SQLQueries.getairlines(search.getSrc()));
                String country = airline.getString("country");
                airlineDB.executeQuery(SQLQueries.addpass(currentUser.getEmail(), name, country));
            }
        }catch (SQLException e){};

        Boolean validT = false;
        Integer tno = 0;
        while (validT == false) {
            ResultSet validticket = airlineDB.executeQuery(SQLQueries.checktno(tno));
            try{
                if (!validticket.next()){
                    validT = true;
                }
                else{
                    Random r = new Random();
                    tno = r.nextInt(1000000);
                }

            }catch (SQLException e){};
        }
        try{
            airlineDB.executeUpdate(SQLQueries.startTran());
            ResultSet validseat = airlineDB.executeQuery(SQLQueries.assertroom());
            if (!validseat.next()){
                System.out.println("No seats left");
                airlineDB.executeUpdate(SQLQueries.finishTran());
                return;
            }
            airlineDB.executeUpdate(SQLQueries.ticketupdate(currentUser.getEmail(),name, tno, Integer.valueOf(search.getPrice())));
            ResultSet fares1 = airlineDB.executeQuery(SQLQueries.getfare(search.getFlightNo1()));
            String fare1 = fares1.getString("fare");
            ResultSet fares2 = airlineDB.executeQuery(SQLQueries.getfare(search.getFlightNo2()));
            String fare2 = fares2.getString("fare");

            if (search.getFlightNo2()==null){
                airlineDB.executeUpdate(SQLQueries.bookingupdate(tno, search.getFlightNo1(), fare1, search.getDepdate(),
                        Integer.valueOf(search.getSeats())));

            }
            else{
                airlineDB.executeUpdate(SQLQueries.bookingupdate(tno, search.getFlightNo1(), fare1, search.getDepdate(),
                        Integer.valueOf(search.getSeats())));
                airlineDB.executeUpdate(SQLQueries.bookingupdate(tno, search.getFlightNo2(), fare2, search.getDepdate(),
                        Integer.valueOf(search.getSeats())));
            }
            airlineDB.executeUpdate(SQLQueries.finishTran());
            System.out.println("You made a booking with the ticket number: " + tno);
        }catch (Exception e) {
            System.err.println("Failed to make a booking");
        }
    }


    public ArrayList<SearchResults> listflights(UserSearch search, Boolean con){
        ArrayList<SearchResults> listflights = new ArrayList();
        airlineDB.executeQuery(SQLQueries.dropAFview());
        airlineDB.executeQuery(SQLQueries.dropOCview());
        airlineDB.executeQuery(SQLQueries.createOCview());
        airlineDB.executeQuery(SQLQueries.createAFview());
        if (con == false) {
            ResultSet results = airlineDB.executeQuery(SQLQueries.userSearchQuery(search.getSrc(), search.getDst(), search.getDepdate()));
            try {
                while (results.next()) {
                    listflights.add(new SearchResults(results));
                }
            } catch (SQLException e) {
            }
        }
        else{
            ResultSet results = airlineDB.executeQuery(SQLQueries.userCSearchQuery(search.getSrc(), search.getDst(), search.getDepdate()));
            try {
                while (results.next()) {
                    listflights.add(new SearchResults(results));
                }
            } catch (SQLException e) {
            }
        }
        airlineDB.executeQuery(SQLQueries.dropAFview());
        airlineDB.executeQuery(SQLQueries.dropOCview());
        return listflights;
    }

    public ArrayList<ScheduledFlight> recordArrival() {
        ArrayList<ScheduledFlight> flights = new ArrayList();
        ResultSet results = airlineDB.executeQuery(SQLQueries.allScheduledFlights());
        try {
            while (results.next()) {
                flights.add(new ScheduledFlight(results));
            }
        } catch (SQLException e) {
        }
        return flights;
    }


    public Boolean recordArrival(ScheduledFlight flight) {
            return airlineDB.executeUpdate(SQLQueries.
                    arrivalUpdate(flight.getActArrTime(), flight.getFlightNo(), flight.getDepDate()));
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

    public Boolean recordDeparture(ScheduledFlight flight) {
        return airlineDB.executeUpdate(SQLQueries.
                    departureUpdate(flight.getActDepTime(), flight.getFlightNo(), flight.getDepDate()));
    }
}
