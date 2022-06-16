/*
 * Filename: ReadConfigFile.java
 * Author: Dylan Russell
 * Purpose: Try to read the properties file in the framework.
 */

package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ReadConfigFile {
	
	public final String FILEPATH = "./src/test/java/propFiles/Automation.properties";
	public Properties properties;
	BufferedReader reader;
	
	// try to read the Automation.properties file
	public ReadConfigFile() {
		try {
			reader = new BufferedReader(new FileReader(FILEPATH));
			properties = new Properties();
			try {
				properties.load(reader);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // end inner try catch
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Properties file could not be found");
		} // end outer try catch
	} // end constructor
	
	public String getTestURL() throws Exception {
		String url = properties.getProperty("testURL");
		if (url != null) 
			return url;
		else throw new RuntimeException("Test URL is not in the property file");
	}
	
} // end class