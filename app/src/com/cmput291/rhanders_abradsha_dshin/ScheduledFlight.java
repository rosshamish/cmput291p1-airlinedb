package com.cmput291.rhanders_abradsha_dshin;
import java.sql.ResultSet;
/**
 * Created by bradshawz on 25/10/15.
 */
public class ScheduledFlight {

    private String flightNo;

    public String getFlightNo() {
        return flightNo;
    }

    public String getDepDate() {
        return depDate;
    }

    public String getActDepTime() {
        return actDepTime;
    }

    public String getActArrTime() {
        return actArrTime;
    }

    private String depDate;
    private String actDepTime;
    private String actArrTime;

    public ScheduledFlight(ResultSet row) {
        try {
            this.flightNo = row.getString("flightno");
            this.depDate = row.getString("dep_date");
            this.actDepTime = row.getString("act_dep_time");
            this.actArrTime = row.getString("act_arr_time");
        } catch (Exception e) {
            System.out.println("oops missed a row");
        }
    }

    public ScheduledFlight(String flightNo, String depDate, String actArrTime, String actDepTime) {
        this.flightNo = flightNo;
        this.depDate = depDate;
        this.actArrTime = actArrTime;
        this.actDepTime = actDepTime;
    }

    public static String rowDescription() {
        return "flightno\tdep_date\tact_dep_time\tact_arr_time";
    }

    @Override
    public String toString() {
        return flightNo + "\t" + depDate + "\t" + actArrTime + "\t" + actDepTime;
    }
}
