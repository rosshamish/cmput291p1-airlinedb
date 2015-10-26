package com.cmput291.rhanders_abradsha_dshin;

import java.sql.ResultSet;

/**
 * Created by bradshawz on 25/10/15.
 */
public class SimpleBooking {
    private String ticketNo;
    private String passName;
    private String depDate;
    private String price;
    private String flightNo;
    private String fare;
    private String seat;
    private String email;

    public String getFlightNo() {return this.flightNo; }
    public String getTicketNo() {return this.ticketNo; }
    public String getDepDate() {return this.depDate; }

    public SimpleBooking(ResultSet row) {
        try {
            //Needed in basic viewing
            this.ticketNo = row.getString("tno");
            this.passName = row.getString("name");
            this.depDate = row.getString("dep_date").substring(0,10);
            this.price = row.getString("paid_price");
            //Needed in descriptive viewing
            this.flightNo = row.getString("flightno");
            this.fare = row.getString("fare");
            this.seat = row.getString("seat");
            this.email = row.getString("email");

        } catch (Exception e) {
            System.out.println("oops missed a row");
        }
    }

    public static String rowDescription() {
        return "tno\tname\tdep_date\tpaid_price";
    }

    public static String descriptiveRowDescription() { return rowDescription() + "\tflightno\tfare\tseat\temail"; }

    public String descriptiveToString() {
        return this.toString() + "\t" + flightNo + "\t" + fare + "\t" + seat + "\t" + email;
    }

    @Override
    public String toString() {
        return ticketNo + "\t" + passName + "\t" + depDate + "\t" + price;
    }
}
