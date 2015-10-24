package com.cmput291.rhanders_abradsha_dshin;

/**
 * Created by ross on 15-10-21.
 */
public class Credentials {
    private String user;
    private char[] pass;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return String.valueOf(pass);
    }

    public void setPass(char[] pass) {
        this.pass = pass;
    }
}
