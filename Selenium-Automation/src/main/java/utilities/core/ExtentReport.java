/*
 * Filename: ExtentReport.java
 * Purpose: Generates the Extent Report test execution report.
 *
 */


package utilities.core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReport {

    public static ExtentSparkReporter sparkReporter = new ExtentSparkReporter(Constants.EXTENT_FOLDER_PATH + "\\Extent-Report.html");
    public static ExtentReports extent = new ExtentReports();
    public static ExtentTest extentTest;

    public static void startReporting() {
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("Browser", Hooks.browser);

        sparkReporter.config().setDocumentTitle("Selenium Test Execution Report");
        sparkReporter.config().setReportName("Test Scenarios");
        sparkReporter.config().setTheme(Theme.DARK);

        String scenario = Hooks.scenario.get().getName();
        extentTest = extent.createTest(scenario);

        extentTest.log(Status.INFO, "Starting Test Execution");
    } // end startReporting()

    public static void endReporting() {
        extent.flush();
    } // end endReporting()

} // end ExtentReport.java
