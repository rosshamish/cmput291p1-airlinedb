package com.cmput291.rhanders_abradsha_dshin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

/**
 * Created by ross on 15-10-21.
 */
public class AirlineDB {
    private final String db_url = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
    private final String db_driverName = "oracle.jdbc.driver.OracleDriver";
    private Class drvClass;
    private Connection connection;

    private Credentials dbCreds;

    public AirlineDB(Credentials dbCreds) {
        this.dbCreds = dbCreds;
        if (!this.connect()) {
            System.err.println("AirlineDB failed to initialize");
        }
    }

    private Boolean connect() {
        try {
            drvClass = Class.forName(db_driverName);
            connection = DriverManager.getConnection(db_url, dbCreds.getUser(), dbCreds.getPassString());
        } catch (ClassNotFoundException e) {
            System.err.printf("Oracle driver ClassNotFoundException: %s\n", e.getMessage());
            return false;
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
            System.err.printf("SQLException: %s\n", e.getMessage());
            return null;
        }
        return resultSet;
    }

    public ResultSet executeQueryFromFile(String queryFilename) {
        return executeQuery(readFileIntoString(queryFilename));
    }

    public void executeUpdate(String update) {
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate(update);
        } catch (SQLException e) {
            System.err.printf("SQLException: %s\n", e.getMessage());
        }
    }

    public void executeUpdateFromFile(String updateFilename) {
        executeUpdate(readFileIntoString(updateFilename));
    }

    private String readFileIntoString(String filename) {
        String path = System.getProperty("user.dir");
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path + "/filename"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(encoded, Charset.defaultCharset());
    }
}
