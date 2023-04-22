package io.github.dylanrusselldev.utilities.helpers;

import io.github.dylanrusselldev.utilities.core.LoggerClass;
import io.github.dylanrusselldev.utilities.core.ReadConfigFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Filename: DatabaseDriver.java
 * Purpose: Enables the use of connecting to a database in order to perform validations through it
 */
public class DatabaseDriver {

    // Connection to DB for queries
    private static Connection connection;
    private final ReadConfigFile propReader = new ReadConfigFile();

    // ReadConfigFile object to read from properties files
    private static ReadConfigFile propFile = new ReadConfigFile();

    private static final LoggerClass LOGGER = new LoggerClass(DatabaseDriver.class);

    public DatabaseDriver() {

        // Attempt to connect to the DB using the information in the properties file
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = propFile.properties.getProperty("dbURL");
            String username = propFile.properties.getProperty("dbUser");
            String password = propFile.properties.getProperty("dbPass");
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);

        } catch (Exception e) {

            LOGGER.errorAndFail("Could not establish connection to the Database.", e);

        } // end try-catch

    } // end constructor

    // Closes the connection to the Database
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

    public String queryData(String query) throws Exception {
        String strData = null;
        ResultSet rs = getResult(query);

        while (rs.next()) {

            strData = rs.getString(1);

        } // end while

        LOGGER.info("Query Result: " + strData);

        return strData;
    } // end queryData()

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
