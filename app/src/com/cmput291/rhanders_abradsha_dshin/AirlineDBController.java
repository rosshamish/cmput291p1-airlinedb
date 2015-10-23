package com.cmput291.rhanders_abradsha_dshin;

import com.sun.tools.corba.se.idl.constExpr.Not;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.SQLInvalidAuthorizationSpecException;

/**
 * Created by ross on 15-10-23.
 */
public class AirlineDBController {
    private AirlineDB airlineDB;

    public void connect(Credentials dbCreds) throws SQLInvalidAuthorizationSpecException {
        try {
            airlineDB = new AirlineDB(dbCreds);
        } catch (SQLInvalidAuthorizationSpecException e) {
            System.err.println("AirlineDBController failed to connect to database");
            throw e;
        }
    }

    public void disconnect() {
        airlineDB.disconnect();
    }

    public Boolean logout() {
        throw new NotImplementedException();
    }

    public boolean isUserLoggedIn(Credentials loginCreds) {
        throw new NotImplementedException();
    }

    public void login(Credentials loginCreds) {
        throw new NotImplementedException();
    }

    public void register(UserDetails newUserDetails) {
        throw new NotImplementedException();
    }
}
