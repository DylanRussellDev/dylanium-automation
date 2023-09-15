package io.github.dylanrusselldev.utilities.database;

import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Filename: DatabaseDriver.java
 * Author: Dylan Russell
 * Purpose: Enables the use of connecting to a database in order to perform validations through it
 */
public class DatabaseDriver {

    private Connection connection;
    private static final LoggerClass LOGGER = new LoggerClass(DatabaseDriver.class);
    private static final ReadConfigFile readConfigFile = new ReadConfigFile();

    public DatabaseDriver() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = readConfigFile.properties.getProperty("dbURL");
            String username = readConfigFile.properties.getProperty("dbUser");
            String password = readConfigFile.properties.getProperty("dbPass");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            LOGGER.fail("Could not establish connection to the Database", e);
        }

    }

    /**
     * Creates a new connection to the database with the specified URL,
     * username, and password.
     *
     * @param url      the URL of the database to connect to
     * @param username the username to use for authentication
     * @param password the password to use for authentication
     */
    private void createConnection(String url, String username, String password) {

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            LOGGER.fail("Failed to connect to database using URL [" + url + "]", e);
        }

    }

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {

        try {

            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                LOGGER.info("Connection to the Database is closed");
            }

        } catch (SQLException e) {
            LOGGER.fail("Unable to close the connection to the Database", e);
        }

    }

    /**
     * Get the result returned from an executed query statement.
     *
     * @param query the query to execute
     * @return the string result from the query
     */
    public String queryData(String query) throws SQLException {
        String strData = null;
        ResultSet rs = getResult(query);

        while (rs.next()) {
            strData = rs.getString(1);
        }

        LOGGER.info("Query Result: " + strData);
        return strData;
    }

    /**
     * Executes a given query.
     *
     * @param query the query to execute
     * @return the query result as a ResultSet
     */
    private ResultSet getResult(String query) throws SQLException {

        if (connection == null || connection.isClosed()) {
            LOGGER.fail("There is not active connection to the database.");
        }

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setQueryTimeout(Integer.parseInt(String.valueOf(Constants.TIMEOUT)));
            return ps.executeQuery();
        } catch (Exception e) {
            LOGGER.fail("Error encountered when executing query: " + query, e);
            return null;
        }

    }

}
