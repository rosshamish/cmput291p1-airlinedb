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
            this.seats = row.getString("seats");
        } catch (Exception e) {
            System.out.println("oops missed a row");
        }
    }

    public static String rowDes() {
        return String.format("     %10s|%10s|%4s|%4s|%10s|%10s|%15s|%10s|%7s|%16s|",
                "flightno1", "flightno2", "src", "dst",
                "dep_time", "arr_time", "connections", "layover",
                "price", "available_seats");
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getDepdate() {
        return depdate;
    }

    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }

    public String getFlightNo1() {
        return flightNo1;
    }

    public String getFlightNo2() {
        return flightNo2;
    }

    public String getActDepTime() {
        return actDepTime;
    }

    public String getActArrTime() {
        return actArrTime;
    }

    public String getStops() {
        return stops;
    }

    public String getLayover() {
        return layover;
    }

    public String getPrice() {
        return price;
    }

    public String getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return String.format("%10s|%10s|%4s|%4s|%10s|%10s|%15s|%10s|%7s|%16s|",
                flightNo1, flightNo2, src, dst,
                actArrTime, actDepTime, stops, layover,
                price, seats);
    }
}
