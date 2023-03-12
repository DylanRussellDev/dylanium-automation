/*
 * Filename: ReadConfigFile.java
 * Purpose: Enables the use of reading data of the properties file.
 */

package io.github.dylanrusselldev.utilities.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.fail;

public class ReadConfigFile {

    public Properties properties;

    // Attempt to read the properties file
    public ReadConfigFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(Constants.PROP_FILEPATH + "Automation.properties"));
            properties = new Properties();

            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                fail("Could not load properties file");
            } // end inner try catch

        } catch (FileNotFoundException e) {
            fail("The properties file could not be found at this location: "
                    + Constants.PROP_FILEPATH + "Automation.properties");
        } // end outer try catch

    } // end constructor

} // end ReadConfigFile.java