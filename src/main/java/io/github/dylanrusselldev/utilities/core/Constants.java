/*
 * Filename: Constants.java
 * Author: Dylan Russell
 * Purpose: Constant variables to call without the need of redeclaring them each time.
 */

package io.github.dylanrusselldev.utilities.core;

import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;

public class Constants {

    private static final ReadConfigFile readFile = new ReadConfigFile();

    public static final Long TIMEOUT = Long.parseLong(readFile.properties.getProperty("timeout"));

    // Browser Updates
    public static final String CHECKING_FOR_UPDATES = "Checking for updates";
    public static final String IS_UP_TO_DATE = "is up to date";
    public static final String UPDATING = "Updating";
    public static final String CHROME_RELAUNCH = "Nearly up to date";
    public static final String EDGE_RELAUNCH = "restart Microsoft Edge";

    //***** FOLDERS & FILEPATHS *****

    // Masterthought Report
    public static final String MASTERTHOUGHT_REPORT_PATH = System.getProperty("user.dir") + "\\target\\Execution-Resources\\masterthought-report\\";
    public static final String CUCUMBER_HTML_REPORT_PATH = "target/Execution-Resources/cucumber-reports/cucumber-report.html";
    public static final String CUCUMBER_JSON_REPORT_PATH = "target/Execution-Resources/cucumber-reports/cucumber.json";

    // Screen Recorder constants
    public static final String VIDEO_FOLDER_PATH = System.getProperty("user.dir") + "\\target\\Execution-Resources\\video-recordings\\";

    // Log file path
    public static final String LOG_FOLDER_PATH = System.getProperty("user.dir") + "\\target\\Execution-Resources\\log-files\\";

    // PDF Download constants
    public static final String TARGET_FILE_DOWNLOADS = System.getProperty("user.dir") + "\\target\\Execution-Resources\\file-downloads\\";

    // System download directory
    public static final String DOWNLOAD_DIRECTORY = System.getProperty("user.home") + "\\Downloads\\";

    // Properties file path
    public static final String PROP_FILEPATH = ".//src//test//resources//properties//";

} // end Constants.java
