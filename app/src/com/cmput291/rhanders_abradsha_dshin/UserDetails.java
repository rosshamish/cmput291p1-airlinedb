package com.cmput291.rhanders_abradsha_dshin;

import java.util.Date;

/**
 * Created by ross on 15-10-23.
 */
public class UserDetails {
    private char[] pass;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return String.valueOf(pass);
    }

    public void setPass(char[] pass) {
        this.pass = pass;
    }
}
