/*
 * Filename: Constants.java
 * Purpose: This file houses constants to call to reference without the need of redeclaring them each time.
 */

package utilities.core;

public class Constants {

    private static final ReadConfigFile readFile = new ReadConfigFile();

    public static final Long TIMEOUT = Long.parseLong(readFile.properties.getProperty("Timeout"));

    // Browser Updates
    public static final String CHECKING_FOR_UPDATES = "Checking for updates";
    public static final String IS_UP_TO_DATE = "is up to date";
    public static final String UPDATING = "Updating";
    public static final String CHROME_RELAUNCH = "Nearly up to date";
    public static final String EDGE_RELAUNCH = "restart Microsoft Edge";

    // Screen Recorder constants
    public static final String VIDEO_FILE_PATH = System.getProperty("user.dir") + "\\target\\video-recordings\\";

} // end Constants.java
