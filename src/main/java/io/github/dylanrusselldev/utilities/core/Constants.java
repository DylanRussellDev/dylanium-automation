package io.github.dylanrusselldev.utilities.core;

/*
 * Filename: Constants.java
 * Author: Dylan Russell
 * Purpose: Constant variables to call without the need of redeclaring them each time.
 */
public class Constants {

    public static final Long TIMEOUT = Long.parseLong("60");

    // test-results folder
    private static final String TEST_RESULTS = System.getProperty("user.dir") + "\\test-results";

    // Masterthought Report
    public static final String MASTERTHOUGHT_REPORT_PATH =  TEST_RESULTS + "\\masterthought-report";
    public static final String CUCUMBER_HTML_REPORT_PATH = "test-results/cucumber-reports/cucumber-report.html";
    public static final String CUCUMBER_JSON_REPORT_PATH = "test-results/cucumber-reports/cucumber.json";

    // Screen Recorder folder
    public static final String VIDEO_FOLDER_PATH = TEST_RESULTS + "\\video-recordings";

    // Log file folder
    public static final String LOG_FOLDER_PATH = TEST_RESULTS + "\\log-files";

    // PDF Download folder
    public static final String TARGET_FILE_DOWNLOADS = TEST_RESULTS + "\\file-downloads";

    // System download folder
    public static final String DOWNLOAD_DIRECTORY = System.getProperty("user.home") + "\\Downloads";

    // Properties file path
    public static final String PROP_FILEPATH = System.getProperty("user.dir") + "\\src\\test\\resources\\properties";

}
