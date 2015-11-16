package com.cmput291.rhanders_abradsha_dshin;

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
            if (Debugging.isEnabled()) {
                System.err.println("AirlineDBController failed to connect to database");
            }
            throw e;
        }
        try {
            //airlineDB.executeUpdate(SQLQueries.dropAFview());
            //airlineDB.executeUpdate(SQLQueries.dropOCview());
        } catch (Exception e) {
        }

        try {
            airlineDB.executeUpdate(SQLQueries.createAFview());
            airlineDB.executeUpdate(SQLQueries.createOCview());
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("creating views, SQLException: " + e.getMessage());
            }
        }
    }

    public void disconnect() {
        try {
            airlineDB.executeUpdate(SQLQueries.dropAFview());
            airlineDB.executeUpdate(SQLQueries.dropOCview());
        } catch (Exception e) {
        }
        airlineDB.disconnect();
    }

    public void register(UserDetails newUserDetails) {
        newUserDetails.setAgent(false);

        if (userExists(newUserDetails)) {
            return;
        }

        try {
            airlineDB.executeUpdate(SQLQueries.userUpdate(newUserDetails.getEmail(),
                    newUserDetails.getPass()));
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("register, SQLException: " + e.getMessage());
            }
        }
    }

    public boolean userExists(UserDetails details) {
        String email = details.getEmail();
        String pass = details.getPass();
        details.setAgent(false);
        ResultSet uresults = airlineDB.executeQuery(SQLQueries.selectUserWith(email));
        ResultSet aresults = airlineDB.executeQuery(SQLQueries.selectAgentWith(email));

        try {
            if (!uresults.next()) {
                return false;
            }
            if (aresults.next()) {
                details.setAgent(true);
            }
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }

        return true;
    }

    public boolean isUserLoggedIn(UserDetails details) {
        return currentUser.getEmail().equals(details.getEmail());
    }

    public void login(UserDetails details) {
        ResultSet aresults = airlineDB.executeQuery(SQLQueries.selectAgentWith(details.getEmail()));
        try {
            if (aresults.next()) {
                details.setAgent(true);
            }
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("SQLException: " + e.getMessage());
            }
        }
        currentUser = details;
    }

    public UserDetails getLoggedInUser() {
        return currentUser;
    }

    public Boolean logout() {
        String email = currentUser.getEmail();
        currentUser = null;
        try {
            airlineDB.executeUpdate(SQLQueries.updateLastLogin(email));
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("logout, SQLException: " + e.getMessage());
            }
        }
        return true;
    }

    public Boolean isAgent() {
        return currentUser.getAgent();
    }

    public BookingStatus attemptBookFlight(String name, String country, SearchResults flight) {
        ResultSet passengers = airlineDB.executeQuery(SQLQueries.selectPassengersWith(name));
        try {
            if (!passengers.next()) {
                airlineDB.executeUpdate(SQLQueries.addPassenger(currentUser.getEmail(), name, country));
            }
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("in passengers, SQLException: " + e.getMessage());
            }
        }

        Boolean validT = false;
        Integer tno = 0;
        while (validT == false) {
            ResultSet validticket = airlineDB.executeQuery(SQLQueries.checktno(tno));
            try {
                if (!validticket.next()) {
                    validT = true;
                } else {
                    Random r = new Random();
                    tno = r.nextInt(1000000);
                }

            } catch (SQLException e) {
                if (Debugging.isEnabled()) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
        }

        try {
            airlineDB.startTransaction();

            // TODO make this query work
//             ResultSet validseat = airlineDB.executeQuery(SQLQueries.assertroom());
//             if (!validseat.next()) {
//                 airlineDB.commitTransaction();
//                 return new BookingStatus(BookingStatus.State.FAIL_NO_SEATS);
//             }

            airlineDB.executeUpdate(SQLQueries.ticketupdate(currentUser.getEmail(), name, tno, Integer.valueOf(flight.getPrice())));

            ResultSet fares1 = airlineDB.executeQuery(SQLQueries.getfare(flight.getFlightNo1()));
            fares1.next();
            String fare1 = fares1.getString("fare");

            airlineDB.executeUpdate(SQLQueries.bookingupdate(tno, flight.getFlightNo1(), fare1, flight.getDepdate(),
                    Integer.valueOf(flight.getSeats())));

            if (flight.getFlightNo2() != null) {
                ResultSet fares2 = airlineDB.executeQuery(SQLQueries.getfare(flight.getFlightNo2()));
                String fare2 = fares2.getString("fare");
                airlineDB.executeUpdate(SQLQueries.bookingupdate(tno, flight.getFlightNo2(), fare2, flight.getDepdate(),
                        Integer.valueOf(flight.getSeats())));
            }

            airlineDB.commitTransaction();
        } catch (Exception e) {
            if (Debugging.isEnabled()) {
                System.err.println("attemptBookFlight, Exception: " + e.getMessage());
            }

            airlineDB.rollbackTransaction();
            return new BookingStatus(BookingStatus.State.FAIL_NO_REASON);
        }
        return new BookingStatus(tno);
    }

    public ArrayList<SearchResults> listFlights(UserSearch search, Boolean connectionsOK) {
        ArrayList<SearchResults> flightsList = new ArrayList();
        String userSearchQuery;
        if (connectionsOK) {
            userSearchQuery = SQLQueries.selectConFlightsWith(search.getSrc(),
                    search.getDst(), search.getDepdate());
        } else {
            if (Debugging.isEnabled()) {
                System.out.println(String.format("\ngetSrc(): %s, getDst(): %s, getDepdate(): %s",
                        search.getSrc(), search.getDst(), search.getDepdate()));
            }
            userSearchQuery = SQLQueries.selectFlightsWith(search.getSrc(),
                    search.getDst(), search.getDepdate());
        }

        ResultSet results = airlineDB.executeQuery(userSearchQuery);
        try {
            while (results.next()) {
                flightsList.add(new SearchResults(results));
            }
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("in listFlights, SQLException: " + e.getMessage());
            }
        }
        return flightsList;
    }

    public ArrayList<SimpleBooking> listBookings() {
        ArrayList<SimpleBooking> bookings = new ArrayList();
        ResultSet results = airlineDB.executeQuery(SQLQueries.userBookingsQuery(currentUser.getEmail()));
        try {
            while (results.next()) {
                bookings.add(new SimpleBooking(results));
            }
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("in listBookings, SQLException: " + e.getMessage());
            }
        }
        return bookings;
    }

    public void deleteBooking(SimpleBooking booking) {
        airlineDB.startTransaction();
        try {
            airlineDB.executeUpdate(SQLQueries.cancelBookingUpdate(booking.getTicketNo(),
                    booking.getFlightNo(), booking.getDepDate()));
            airlineDB.executeUpdate(SQLQueries.cancelTicketUpdate(booking.getTicketNo()));
            airlineDB.commitTransaction();
        } catch (SQLException e) {
            airlineDB.rollbackTransaction();
        }
    }

    public Boolean recordArrival(ScheduledFlight flight) {
        try {
            return airlineDB.executeUpdate(SQLQueries.
                    arrivalUpdate(flight.getActArrTime(), flight.getFlightNo(), flight.getDepDate()));
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("recordArrival, SQLException: " + e.getMessage());
            }
            return false;
        }
    }

    public Boolean recordDeparture(ScheduledFlight flight) {
        try {
            return airlineDB.executeUpdate(SQLQueries.
                    departureUpdate(flight.getActDepTime(), flight.getFlightNo(), flight.getDepDate()));
        } catch (SQLException e) {
            if (Debugging.isEnabled()) {
                System.err.println("recordDeparture, SQLException: " + e.getMessage());
            }
            return false;
        }
    }
}
