/*
 * Filename: ReadConfigFile.java
 * Author: Dylan Russell
 * Purpose: Enables the use of reading data of the properties file.
 */

package io.github.dylanrusselldev.utilities.filereaders;

import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.core.LoggerClass;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ReadConfigFile {

    public Properties properties;
    private static final LoggerClass LOGGER = new LoggerClass(ReadConfigFile.class);

    /**
     * Load the properties file
     */
    public ReadConfigFile() {
        BufferedReader reader;
        String path = Constants.PROP_FILEPATH + "Automation.properties";

        try {
            reader = new BufferedReader(new FileReader(path));
            properties = new Properties();

            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                LOGGER.logAndFail(Level.ERROR, "Could not load the properties file", e);
            } // end inner try catch

        } catch (FileNotFoundException e) {
            LOGGER.logAndFail(Level.ERROR, "The properties file could not be found at this location: "
                    + Constants.PROP_FILEPATH + "Automation.properties", e);
        } // end outer try catch

    } // end constructor

} // end ReadConfigFile.java