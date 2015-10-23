package com.cmput291.rhanders_abradsha_dshin;

/**
 * Created by ross on 15-10-23.
 */
public class AirlineDBController {
    private AirlineDB airlineDB;

    public void connect(Credentials dbCreds) {
        airlineDB = new AirlineDB(dbCreds);
    }

    public void disconnect() {
        airlineDB.disconnect();
    }
}
