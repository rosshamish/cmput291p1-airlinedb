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
        return "UPDATE sch_flights SET act_dep_time = to_date('" + time +
                "', 'hh24:mi') WHERE flightno = '" + flightNo + "' AND dep_date " +
                "= '" + depDate + "'";
    }

    public static String arrivalUpdate(String time, String flightNo, String depDate){
        return "UPDATE sch_flights SET act_arr_time = to_date('" + time +
                "', 'hh24:mi') WHERE flightno = '" + flightNo + "' AND dep_date " +
                "= '" + depDate + "'";
    }

    public static String lastLoginUpdate(String email){
        return "UPDATE users SET last_login = sysdate WHERE email = '" + email + "'";
    }

    public static String loginU(String email, String pass){
        return "SELECT email, pass FROM users WHERE email ='" + email + "'AND pass ='" + pass + "'";
    }

    public static String loginA(String email){
        return "SELECT email FROM airline_agents WHERE email ='" + email + "'";
    }

    public static String checkusr(String email) {
        return "SELECT email FROM users WHERE email ='" + email + "'";
    }


    public static String allScheduledFlights() {
        return "SELECT * FROM sch_flights";
    }

    public static String userSearchQuery(String src, String dst, String deptime){
        return "hi";
    }

    public static String bookingupdate(String name, SearchResults results){
        return "hi";
    }


    public static String userUpdate(String email, String pass) {
        return "INSERT INTO users VALUES('" + email + "','" + pass + "', null)";
    }
}
