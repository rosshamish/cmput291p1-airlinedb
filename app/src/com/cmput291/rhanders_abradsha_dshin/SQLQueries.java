package com.cmput291.rhanders_abradsha_dshin;
/**
 * Created by ross on 15-10-23.
 */
public class SQLQueries {

    public static String userBookingsQuery(String email) {
        return "SELECT * FROM tickets, bookings WHERE tickets.tno = bookings.tno and tickets.email = '" + email + "'";
    }

    public static String cancelBookingUpdate(String ticketNo, String flightNo, String depDate) {
        return "";
    }

    public static String departureUpdate(String time, String flightNo, String depDate){
        return "UPDATE sch_flights SET act_dep_time = '" + time +
                "' WHERE flightno = '" + flightNo + "' and dep_date = '" + depDate + "'";
    }

    public static String arrivalUpdate(String time, String flightNo, String depDate){
        return "UPDATE sch_flights SET act_arr_time = '" + time +
                "' WHERE flightno = '" + flightNo + "' and dep_date = '" + depDate + "'";
    }

    public static String lastLoginUpdate(String email){
        return "UPDATE users SET last_login = sysdate WHERE email = '" + email + "'";
    }

    public static String login(String email, String pass){
        return "SELECT email, pass FROM users WHERE email = '" + email + "' AND pass = '" + pass + "'";
    }

    public static String allScheduledFlights() {
        return "SELECT * FROM sch_flights";
    }

    public static String userUpdate(String email, String pass) {
        return "INSERT INTO users VALUES('" + email + "','" + pass + "', null)";
    }
}
