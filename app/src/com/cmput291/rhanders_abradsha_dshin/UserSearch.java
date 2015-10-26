package com.cmput291.rhanders_abradsha_dshin;

import java.sql.ResultSet;

/**
 * Created by daniel on 2015-10-25.
 */
public class UserSearch {
    private String src;
    private String dst;
    private String depdate;


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

}
