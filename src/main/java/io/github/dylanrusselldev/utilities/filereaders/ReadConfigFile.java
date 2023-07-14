/*
 * Filename: ReadConfigFile.java
 * Author: Dylan Russell
 * Purpose: Enables the use of reading data of the properties file.
 */

package io.github.dylanrusselldev.utilities.filereaders;

import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.reporting.LoggerClass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ReadConfigFile {

    public Properties properties;
    private static final LoggerClass LOGGER = new LoggerClass(ReadConfigFile.class);

    /**
     * Load the properties file.
     */
    public ReadConfigFile() {
        BufferedReader bufferedReader;
        String path = Constants.PROP_FILEPATH + "Automation.properties";

        try {
            bufferedReader = new BufferedReader(new FileReader(path));
            properties = new Properties();

            try {
                properties.load(bufferedReader);
                bufferedReader.close();
            } catch (IOException e) {
                LOGGER.logAndFail("Could not load the properties file", e);
            } // end inner try catch

        } catch (FileNotFoundException e) {
            LOGGER.logAndFail("The properties file could not be found at this location: "
                    + Constants.PROP_FILEPATH + "Automation.properties", e);
        }

    }

}
