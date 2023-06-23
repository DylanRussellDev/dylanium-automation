/*
 * Filename: Constants.java
 * Author: Dylan Russell
 * Purpose: Constant variables to call without the need of redeclaring them each time.
 */

package io.github.dylanrusselldev.utilities.core;

import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;

public class Constants {

    private static final ReadConfigFile readConfigFile = new ReadConfigFile();

    public static final Long TIMEOUT = Long.parseLong(readConfigFile.properties.getProperty("timeout"));

    // Browser Updates
    public static final String CHECKING_FOR_UPDATES = "Checking for updates";
    public static final String IS_UP_TO_DATE = "is up to date";
    public static final String UPDATING = "Updating";
    public static final String CHROME_RELAUNCH = "Nearly up to date";
    public static final String EDGE_RELAUNCH = "restart Microsoft Edge";

    //***** FOLDERS & FILEPATHS *****

    public static final String TEST_RESULTS = System.getProperty("user.dir") + "\\test-results";

    // Masterthought Report
    public static final String MASTERTHOUGHT_REPORT_PATH =  TEST_RESULTS + "\\masterthought-report\\";
    public static final String CUCUMBER_HTML_REPORT_PATH = "test-results/cucumber-reports/cucumber-report.html";
    public static final String CUCUMBER_JSON_REPORT_PATH = "test-results/cucumber-reports/cucumber.json";

    // Screen Recorder constants
    public static final String VIDEO_FOLDER_PATH = TEST_RESULTS + "\\video-recordings\\";
    // Log file path
    public static final String LOG_FOLDER_PATH = TEST_RESULTS + "\\log-files\\";
    // PDF Download constants
    public static final String TARGET_FILE_DOWNLOADS = TEST_RESULTS + "\\file-downloads\\";
    // System download directory
    public static final String DOWNLOAD_DIRECTORY = System.getProperty("user.home") + "\\Downloads\\";
    // Properties file path
    public static final String PROP_FILEPATH = ".//src//test//resources//properties//";

} // end Constants.java
