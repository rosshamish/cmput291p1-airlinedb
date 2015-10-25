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
    private UserDetails currentUser;

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

    public boolean logout() {
        airlineDB.executeUpdate(SQLQueries.lastLoginUpdate(currentUser.getEmail()));
        currentUser = null;
        return true;
    }

    public boolean isUserLoggedIn(UserDetails details) {
        String email = details.getEmail();
        String pass = details.getPass();
        details.setAgent(false);
        ResultSet uresults = airlineDB.executeQuery(SQLQueries.loginU(email, pass));
        ResultSet aresults = airlineDB.executeQuery(SQLQueries.loginA(email));

        try {
            if (!uresults.next()) {
                return false;
            }
            if(uresults.next() && aresults.next()){
                details.setAgent(true);            }
        } catch (SQLException e) {}                   //TODO: not sure if this is alright
        return true;
    }

    public void login(UserDetails details) {
        currentUser = details;
    }

    public Boolean isAgent() {
        return currentUser.getAgent();
    }

    public void register(UserDetails newUserDetails) {
        String email = newUserDetails.getEmail();
        String pass = newUserDetails.getPass();
        String update = "INSERT INTO users VALUES(" + email + ',' + pass + ", null)";  // TODO: add to SQLQueries
        ResultSet usrresults = airlineDB.executeQuery(SQLQueries.checkusr(email));
        try {
            if (usrresults.next()){
                System.out.println("This email has already been registered");
            }
        }catch (SQLException e){}
        airlineDB.executeUpdate(update);

    }

    public void recordArrival() {
        throw new NotImplementedException();
    }

    public void recordDeparture() {
        throw new NotImplementedException();
    }
}
