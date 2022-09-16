/*
 * Filename: Constants.java
 * Purpose: This file houses constants to call to reference without the need of
 *          redeclaring them each time.
 */

package utilities.core;

public class Constants {

    private static final ReadConfigFile readFile = new ReadConfigFile();

    public static final Long TIMEOUT = Long.parseLong(readFile.properties.getProperty("Timeout"));

    // Browser Updates
    public static final String CHECKINGFORUPDATES = "Checking for updates";
    public static final String UPTODATE = "is up to date";
    public static final String UPDATING = "Updating";
    public static final String CHROMERELAUNCH = "Nearly up to date";
    public static final String EDGERELAUNCH = "restart Microsoft Edge";

} // end Constants.java
