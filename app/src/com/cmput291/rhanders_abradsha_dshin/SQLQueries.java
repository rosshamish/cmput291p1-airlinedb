package com.cmput291.rhanders_abradsha_dshin;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by ross on 15-10-23.
 */
public class SQLQueries {

    public static String userBookingsQuery(String email) {
        return "SELECT * FROM tickets, bookings WHERE tickets.tno = bookings.tno and tickets.email = '" + email + "'";
    }

    public static String cancelBookingUpdate(String ticketNo, String flightNo, String depDate) {
        return "DELETE FROM bookings b WHERE b.tno = "+ ticketNo +" AND " +
                "b.flightno = '"+ flightNo +"' AND b.dep_date = to_date('"+ depDate +"', 'YYYY-MM-DD')";
    }

    public static String cancelTicketUpdate(String ticketNo) {
        return "DELETE FROM tickets t WHERE t.tno = " + ticketNo;
    }

    public static String departureUpdate(String time, String flightNo, String depDate) {
        return "UPDATE sch_flights SET act_dep_time = to_date('" + time +
                "', 'hh24:mi') WHERE flightno = '" + flightNo + "' AND dep_date " +
                "= '" + depDate + "'";
    }

    public static String arrivalUpdate(String time, String flightNo, String depDate) {
        return "UPDATE sch_flights SET act_arr_time = to_date('" + time +
                "', 'hh24:mi') WHERE flightno = '" + flightNo + "' AND dep_date " +
                "= '" + depDate + "'";
    }

    public static String updateLastLogin(String email) {
        return "UPDATE users SET last_login = sysdate WHERE email = '" + email + "'";
    }

    public static String selectUserWith(String email, String pass) {
        return "SELECT email, pass FROM users WHERE email ='" + email + "'AND pass ='" + pass + "'";
    }

    public static String selectUserWith(String email) {
        return "SELECT email FROM users WHERE email ='" + email + "'";
    }

    public static String selectAgentWith(String email){
        return "SELECT email FROM airline_agents WHERE email ='" + email + "'";
    }

    public static String allScheduledFlights() {
        return "SELECT * FROM sch_flights";
    }

    //TODO: used http://stackoverflow.com/questions/14962970/sql-query-if-value-is-null-then-return-1 for case
    //TODO: used the assignment 2 sql statements solutions as reference (especially the views) credit to drafiei
    public static String dropAFview() {
        return "DROP VIEW avail_flights";
    }
    public static String dropOCview() {
        return "DROP VIEW one_connection";
    }
    public static String createAFview() {
        return "CREATE VIEW avail_flights(flightno, dep_date, src, dst, dep_time, arr_time, fare, seats, price) \n" +
                "AS SELECT f.flightno, sf.dep_date, f.src, f.dst, f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time)), \n" +
                "f.dep_time+(trunc(sf.dep_date)-trunc(f.dep_time))+(f.est_dur/60+a2.tzone-a1.tzone)/24, \n" +
                "fa.fare, fa.limit-count(tno), fa.price \n" +
                "FROM flights f, flight_fares fa, sch_flights sf, bookings b, airports a1, airports a2 \n" +
                "WHERE f.flightno=sf.flightno and f.flightno=fa.flightno and f.src=a1.acode and f.dst=a2.acode \n" +
                "and fa.flightno=b.flightno(+) and fa.fare=b.fare(+) and sf.dep_date=b.dep_date(+) \n" +
                "GROUP BY f.flightno, sf.dep_date, f.src, f.dst, f.dep_time, f.est_dur, a2.tzone, a1.tzone, \n" +
                "fa.fare, fa.limit, fa.price \n" +
                "HAVING fa.limit-count(tno) > 0";
    }

    public static String createOCview() {
        return "CREATE VIEW one_connection(flightno1,flightno2,dep_date,src,dst,dep_time,arr_time,layover,price,seats) \n" +
                "AS SELECT a1.flightno, a2.flightno, a1.dep_date, a1.src, a2.dst, a1.dep_time, a2.arr_time, \n" +
                "a2.dep_time-a1.arr_time, a1.price+a2.price, LEAST(a1.seats, a2.seats) \n" +
                "FROM avail_flights a1, avail_flights a2 \n" +
                "WHERE a1.dst=a2.src";
    }
    public static String selectFlightsWith(String src, String dst, String depdate) {
        return "SELECT flightno1,flightno2,src,dst,to_char(dep_time, 'HH24:MI') as dep_time, \n" +
                "to_char(arr_time,'HH24:MI') as arr_time, CASE WHEN flightno2 IS NULL THEN 0 ELSE 1 END as connections, \n" +
                "layover, price, seats \n" +
                "FROM \n" +
                "((SELECT flightno1, flightno2, src, dst, dep_time, arr_time, layover, price, seats \n" +
                "FROM one_connection WHERE to_char(dep_date,'DD-Mon-YYYY')= '" + depdate + "' and \n" +
                "lower(src) = lower('" + src + "') and lower(dst) = lower('" + dst + "')) \n" +
                "UNION \n" +
                "(SELECT flightno flightno1, '' flightno2, src, dst, dep_time, arr_time, 0 layover, price, seats \n" +
                "FROM avail_flights WHERE to_char(dep_date,'DD-Mon-YYYY')= '" + depdate + "' and \n" +
                "lower(src) LIKE lower('" + src + "') and lower(dst) LIKE lower('" + dst + "'))) \n" +
                "ORDER BY price ASC";
    }

    public static String selectConFlightsWith(String src, String dst, String depdate) {
        return "SELECT flightno1,flightno2,src,dst,to_char(dep_time, 'HH24:MI') as dep_time, " +
                "to_char(arr_time,'HH24:MI') as arr_time, (CASE WHEN a2.flightno2 IS NULL THEN 0 ELSE 1 END) as connections, " +
                "layover, price, seats " +
                "FROM " +
                "((SELECT flightno1, flightno2, src, dst, dep_time, arr_time, layover, price, seats " +
                "FROM one_connection WHERE to_char(dep_date,'DD-Mon-YYYY')= '" + depdate + "' and " +
                "lower(src) = lower('" + src + "') and lower(dst) = lower('" + dst + "')) " +
                "UNION" +
                "(SELECT flightno flightno1, '' flightno2, src, dst, dep_time, arr_time, 0 layover, price, seats " +
                "FROM avail_flights WHERE to_char(dep_date,'DD-Mon-YYYY')= '" + depdate + "' and " +
                "lower(src) LIKE lower('" + src + "') and lower(dst) LIKE lower('" + dst + "'))) " +
                "ORDER BY connections ASC, price ASC";
    }

    public static String assertroom() {
        return "SELECT tno FROM flight_fares fa, bookings WHERE fa.limit-count(tno) > 0";
    }

    public static String ticketupdate(String email, String name, Integer tno, Integer price) {
        return "INSERT INTO tickets VALUES('" + email + "','" + name + "'," + tno + "," + price +")";
    }

    public static String bookingupdate(Integer tno, String flightno, String fare,String depdate, Integer seats) {
        return "INSERT INTO bookings VALUES(" + tno + ",'" + flightno + "','" + fare +
                "', TO_DATE('" + depdate + "','DD-Mon-YYYY')," +  seats +")";
    }

    public static String selectPassengersWith(String name) {
        return "SELECT name FROM passengers WHERE name = '" + name + "'";
    }

    public static String addPassenger(String email, String name, String country) {
        return "INSERT INTO passengers VALUES('" + email + "','" + name + "','" + country + "')";
    }

    public static String getairlines(String src) {
        return "SELECT country FROM airports WHERE acode = '" + src + "'";
    }

    public static String getfare(String flightno) {
        return "SELECT fare FROM flight_fares WHERE flightno = '" + flightno + "'";
    }

    public static String checktno(Integer tno) {
        return "SELECT tno FROM tickets WHERE tno = " + tno;
    }

    public static String userUpdate(String email, String pass) {
        return "INSERT INTO users VALUES('" + email + "','" + pass + "', null)";
    }
}

