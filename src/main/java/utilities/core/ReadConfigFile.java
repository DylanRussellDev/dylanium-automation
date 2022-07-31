/*
 * Filename: ReadConfigFile.java
 * Author: Dylan Russell
 * Purpose: Try to read the properties file in the framework.
 */

package utilities.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.testng.Assert.fail;

public class ReadConfigFile {
	
	public final String FILEPATH = "./src/test/java/propFiles/Automation.properties";
	public Properties properties;
	BufferedReader reader;
	
	// Attempt to read the properties file
	public ReadConfigFile() {
		try {
			reader = new BufferedReader(new FileReader(FILEPATH));
			properties = new Properties();

			try {
				properties.load(reader);
				reader.close();
			} catch (IOException e) {
				fail("Could not load properties file");
			} // end inner try catch

		} catch (FileNotFoundException e) {
			fail("The properties file could not be found");
		} // end outer try catch
	} // end constructor

} // end ReadConfigFile.java