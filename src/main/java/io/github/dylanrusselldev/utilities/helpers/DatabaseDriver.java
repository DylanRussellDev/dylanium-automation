/*
 * Filename: DatabaseDriver.java
 * Author: Dylan Russell
 * Purpose: Enables the use of connecting to a database in order to perform validations through it
 */

package io.github.dylanrusselldev.utilities.helpers;

import io.github.dylanrusselldev.utilities.core.LoggerClass;
import io.github.dylanrusselldev.utilities.core.ReadConfigFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseDriver {

    // Database connection object
    private static Connection connection;

    // ReadConfigFile object to read from properties files
    private static ReadConfigFile propReader = new ReadConfigFile();

    private static final LoggerClass LOGGER = new LoggerClass(DatabaseDriver.class);

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

            LOGGER.errorAndFail("Could not establish connection to the Database.", e);

        } // end try-catch

    } // end constructor

    /**
     * Close the connection to the database
     */
    public void closeConnection() {
        try {

            if (connection != null && !connection.isClosed()) {

                connection.close();
                connection = null;
                LOGGER.info("Connection to the Database is closed.\n");

            } // end if

        } catch (SQLException e) {

            LOGGER.errorAndFail("Unable to close the connection to the Database.", e);

        } // end try-catch

    } // end closeConnection()

    /**
     * Get the result returned from an executed query statement
     *
     * @param query The query to execute
     * @throws Exception
     */
    public String queryData(String query) throws Exception {
        String strData = null;
        ResultSet rs = getResult(query);

        while (rs.next()) {

            strData = rs.getString(1);

        } // end while

        LOGGER.info("Query Result: " + strData);

        return strData;
    } // end queryData()

    /**
     * Executes a given query
     *
     * @param query The query to execute
     * @throws SQLException
     */
    private ResultSet getResult(String query) throws SQLException {

        if (connection == null || connection.isClosed()) {
            LOGGER.errorAndFail("There is not active connection to the database.");
        } // end if

        try {

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setQueryTimeout(Integer.parseInt(propReader.properties.getProperty("timeout")));
            return ps.executeQuery();

        } catch (Exception e) {

            LOGGER.errorAndFail("Error encountered when executing query: " + query, e);
            return null;

        } // end try-catch

    } // end getResult()

} // end DatabaseDriver.java
