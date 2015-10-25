package com.cmput291.rhanders_abradsha_dshin;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public boolean isUserLoggedIn(UserDetails details) {
        String email = details.getEmail();
        String pass = details.getPass();

        String query = "SELECT email, pass FROM users WHERE email =" + email + "AND pass =" + pass + ';';
        ResultSet results = airlineDB.executeQuery(query);
        try {
            if (!results.next()) {
                return false;
            }
        } catch (SQLException e) {}                   //TODO: not sure if this is alright
        return true;
    }



    public void register(UserDetails newUserDetails) {
        String email = newUserDetails.getEmail();
        String pass = newUserDetails.getPass();

        String update = "INSERT INTO users VALUES(" + email + ',' + pass + ", null);";  // TODO: add to SQLQueries
        airlineDB.executeUpdate(update);
    }
}
