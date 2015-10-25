package com.cmput291.rhanders_abradsha_dshin;
import java.sql.ResultSet;
/**
 * Created by bradshawz on 25/10/15.
 */
public class ScheduledFlight {

    private String flightNo;
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

    @Override
    public String toString() {
        return flightNo + "\t" + depDate + "\t" + actArrTime + "\t" + actDepTime;
    }
}
