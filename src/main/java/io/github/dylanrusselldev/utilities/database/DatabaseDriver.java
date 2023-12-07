package io.github.dylanrusselldev.utilities.database;

import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Filename: DatabaseDriver.java
 * Author: Dylan Russell
 * Purpose: Enables the use of connecting to a database in order to perform validations.
 */
public class DatabaseDriver {

    private Connection connection;
    private static final LoggerClass LOGGER = new LoggerClass(DatabaseDriver.class);

    public DatabaseDriver(String databaseType) {
        createConnection(databaseType);
    }

    /**
     * Creates a new connection to the database with the specified URL,
     * username, and password.
     */
    private void createConnection(String databaseType) {
        ReadConfigFile readConfigFile = new ReadConfigFile(Constants.PROP_FILEPATH + "\\database.properties");

        String url = "";

        switch (databaseType.toUpperCase()) {

            case "MYSQL":
                url = readConfigFile.properties.getProperty("urlMySQL");
                break;

            case "ORACLE":
                url = readConfigFile.properties.getProperty("urlOracle");
                break;

            case "POSTGRESQL":
                url = readConfigFile.properties.getProperty("urlPostgreSQL");
                break;

            case "SQL SERVER":
                url = readConfigFile.properties.getProperty("urlSQLServer");
                break;

            default:
                LOGGER.fail("Invalid String argument given for the database type.");
                break;
        }

        url = url
                .replace("{host}", readConfigFile.properties.getProperty("dbHost"))
                .replace("{port}", readConfigFile.properties.getProperty("dbPort"))
                .replace("{databaseName}", readConfigFile.properties.getProperty("dbName"));

        try {
            connection = DriverManager.getConnection(url, readConfigFile.properties.getProperty("dbUser"), CommonMethods.decrypt("dbPass"));
            connection.setAutoCommit(false);
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
    public synchronized List<Map<String, ?>> queryData(String query) {

        LOGGER.info("Attempting to execute query: " + query);

        try {
            ResultSet resultSet = getResult(query);
            LOGGER.info("Result retrieved from the database: " + resultSet);
            return resultSetToList(resultSet);
        } catch (Exception e) {
            closeConnection();
            LOGGER.fail("Failure occurred when attempting to query the database", e);
        } finally {
            closeConnection();
        }

        return null;

    }

    private List<Map<String, ?>> resultSetToList(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        int columns = resultSetMetaData.getColumnCount();

        List<Map<String, ?>> queryResult = new ArrayList<>();

        while (resultSet.next()) {

            Map<String, Object> row = new HashMap<>();

            for (int i = 1; i <= columns; i++) {
                row.put(resultSetMetaData.getColumnLabel(i).toUpperCase(), resultSet.getObject(i));
            }

            queryResult.add(row);

        }

        return queryResult;

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
