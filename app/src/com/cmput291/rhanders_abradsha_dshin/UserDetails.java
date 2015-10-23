package com.cmput291.rhanders_abradsha_dshin;

import java.util.Date;

/**
 * Created by ross on 15-10-23.
 */
public class UserDetails {
    private Credentials creds;
    private String name;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Credentials getCreds() {
        return creds;
    }

    public void setCreds(Credentials creds) {
        this.creds = creds;
    }
}
