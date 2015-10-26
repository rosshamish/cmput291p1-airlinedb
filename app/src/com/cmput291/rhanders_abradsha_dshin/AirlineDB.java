package com.cmput291.rhanders_abradsha_dshin;

import oracle.jdbc.driver.OracleDriver;

import java.sql.*;

/**
 * Created by ross on 15-10-21.
 */
public class AirlineDB {
    private final String db_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
    private Connection connection;

    private Credentials dbCreds;

    public AirlineDB(Credentials dbCreds) throws SQLInvalidAuthorizationSpecException {
        this.dbCreds = dbCreds;
        if (!this.connect()) {
            System.err.println("AirlineDB failed to connect to database");
            throw new SQLInvalidAuthorizationSpecException();
        }
    }

    private Boolean connect() {
        try {
            Driver driver = new OracleDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(db_url, dbCreds.getUser(), dbCreds.getPass());
        } catch (SQLException e) {
            System.err.printf("On connect, SQLException: %s\n", e.getMessage());
            return false;
        }
        return true;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.printf("On disconnect, SQLException: %s\n", e.getMessage());
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            resultSet = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.err.printf("---\nWhile executing query\n\n%s\n\n", query);
            System.err.printf("SQLException: %s\n---\n", e.getMessage());
            return null;
        }
        return resultSet;
    }

    public Boolean executeUpdate(String update) {
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(update);
        } catch (SQLException e) {
            System.err.printf("---\nWhile executing update\n\n%s\n", update);
            System.err.printf("SQLException: %s\n", e.getMessage());
            return false;
        }
        return true;
    }
}
