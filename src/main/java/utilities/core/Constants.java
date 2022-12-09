/*
 * Filename: Constants.java
 * Purpose: This file houses constants to call to reference without the need of redeclaring them each time.
 */

package utilities.core;

public class Constants {

    private static final ReadConfigFile readFile = new ReadConfigFile();

    public static final Long TIMEOUT = Long.parseLong(readFile.properties.getProperty("timeout"));

    // Browser Updates
    public static final String CHECKING_FOR_UPDATES = "Checking for updates";
    public static final String IS_UP_TO_DATE = "is up to date";
    public static final String UPDATING = "Updating";
    public static final String CHROME_RELAUNCH = "Nearly up to date";
    public static final String EDGE_RELAUNCH = "restart Microsoft Edge";

    // Screen Recorder constants
    public static final String VIDEO_FOLDER_PATH = System.getProperty("user.dir") + "\\target\\video-recordings\\";

    // PDF Download constants
    public static final String PDF_FOLDER_PATH = System.getProperty("user.dir") + "\\target\\PDF-Downloads\\";

    // System download directory
    public static final String DOWNLOAD_DIRECTORY = System.getProperty("user.home") + "\\Downloads";

    // Extent report directory
    public static final String EXTENT_FOLDER_PATH = System.getProperty("user.dir") + "\\target\\Extent-Report\\";

} // end Constants.java
