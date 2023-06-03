/*
 * Filename: DatabaseDriver.java
 * Author: Dylan Russell
 * Purpose: Enables the use of connecting to a database in order to perform validations through it
 */

package io.github.dylanrusselldev.utilities.database;

import io.github.dylanrusselldev.utilities.core.LoggerClass;
import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;
import org.slf4j.event.Level;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDriver {

    private Connection connection;
    private static final LoggerClass LOGGER = new LoggerClass(DatabaseDriver.class);
    private static final ReadConfigFile propReader = new ReadConfigFile();

    public DatabaseDriver() {

        // Attempt to connect to the DB using the information in the properties file
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = propReader.properties.getProperty("dbURL");
            String username = propReader.properties.getProperty("dbUser");
            String password = propReader.properties.getProperty("dbPass");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);

        } catch (Exception e) {

            LOGGER.logAndFail("Could not establish connection to the Database", e);

        } // end try-catch

    } // end constructor

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
            LOGGER.logAndFail("Failed to connect to database using URL [" + url + "]", e);
        } // end try-catch
    } // end createConnection()

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {

            if (connection != null && !connection.isClosed()) {

                connection.close();
                connection = null;
                LOGGER.log(Level.INFO, "Connection to the Database is closed");

            } // end if

        } catch (SQLException e) {

            LOGGER.logAndFail("Unable to close the connection to the Database", e);

        } // end try-catch

    } // end closeConnection()

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

        } // end while

        LOGGER.log(Level.INFO, "Query Result: " + strData);

        return strData;
    } // end queryData()

    /**
     * Executes a given query.
     *
     * @param query the query to execute
     * @return the query result as a ResultSet
     */
    private ResultSet getResult(String query) throws SQLException {

        if (connection == null || connection.isClosed()) {
            LOGGER.logAndFail("There is not active connection to the database.");
        } // end if

        try {

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setQueryTimeout(Integer.parseInt(propReader.properties.getProperty("timeout")));
            return ps.executeQuery();

        } catch (Exception e) {

            LOGGER.logAndFail("Error encountered when executing query: " + query, e);
            return null;

        } // end try-catch

    } // end getResult()

} // end DatabaseDriver.java
