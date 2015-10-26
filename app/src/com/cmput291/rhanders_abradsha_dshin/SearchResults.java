package com.cmput291.rhanders_abradsha_dshin;

import java.sql.ResultSet;

/**
 * Created by User on 2015-10-25.
 */
public class SearchResults {
    private String src;
    private String dst;
    private String depdate;

    private String flightNo1;
    private String flightNo2;
    private String actDepTime;
    private String actArrTime;
    private String stops;
    private String layover;
    private String price;
    private String seats;

    public String getSrc() {
        return src;
    }
    public void setSrc(String src) {this.src = src;}

    public String getDst() {return dst;}
    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getDepdate() {return depdate;}
    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }



    public SearchResults(ResultSet row) {
        try {
            this.flightNo1 = row.getString("flightno1");
            this.flightNo2 = row.getString("flightno2");
            this.src = row.getString("src");
            this.dst = row.getString("dst");
            this.actDepTime = row.getString("dep_time");
            this.actArrTime = row.getString("arr_time");
            this.stops = row.getString("connections");
            this.layover = row.getString("layover");
            this.price = row.getString("price");
            this.seats = row.getString("available seats");

        } catch (Exception e) {
            System.out.println("oops missed a row");
        }
    }

    public static String rowDes() {
        return "flightno1\tflightno2\tsrc\tdst\tdep_time\tarr_time\tconnections\tlayover\tprice\tavailable seats";
    }

    @Override
    public String toString() {
        return flightNo1 + "\t" + flightNo2 + "\t" + src + "\t" + dst + "\t" + actArrTime + "\t" + actDepTime
                + "\t" + stops + "\t" + layover + "\t" + price + "\t" + seats;
    }
}
