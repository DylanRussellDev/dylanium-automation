/*
 * Filename: DatabaseDriver.java
 * Purpose: Enables the use of connecting to a database in order to perform validations through it
 */

package utilities.helpers;

import utilities.core.ReadConfigFile;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.testng.Assert.fail;

public class DatabaseDriver {

    // Connection to DB for queries
    private static Connection connection;

    // ReadConfigFile object to read from properties files
    private static ReadConfigFile propFile = new ReadConfigFile();

    public DatabaseDriver() {
        // Attempt to connect to the DB using the information in the properties file
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = propFile.properties.getProperty("dbURL");
            String username = propFile.properties.getProperty("dbUser");
            String password = propFile.properties.getProperty("dbPass");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            fail("Could not establish connection to the Database. Error Message: " + e.getMessage());
        } // end try-catch
    } // end constructor

} // end DatabaseDriver.java
