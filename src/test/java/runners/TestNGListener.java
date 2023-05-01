/*
 * Filename: TestNGListener.java
 * Author: Dylan Russell
 * Purpose: Methods that determine what to do before and after test execution
 */

package runners;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.core.LoggerClass;
import io.github.dylanrusselldev.utilities.core.MasterthoughtReport;
import io.github.dylanrusselldev.utilities.runtime.CommandRunner;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.event.Level;
import org.testng.IExecutionListener;

public class TestNGListener implements IExecutionListener {

    private static final LoggerClass LOGGER = new LoggerClass(TestNGListener.class);

    /**
     * Code that executes before all tests have started.
     */
    public void onExecutionStart() {
        LOGGER.log(Level.INFO, "*** TEST EXECUTION STARTED ***");
    } // end onExecutionStart

    /**
     * Code that executes after all tests have finished.
     */
    public void onExecutionFinish() {

        MasterthoughtReport.generateTestReport();

        CommandRunner.endDriverExe();

        LOGGER.log(Level.INFO, "*** TEST EXECUTION FINISHED ***");
        LOGGER.log(Level.INFO, "View Report here: " + Constants.MASTERTHOUGHT_REPORT_PATH);
        LOGGER.log(Level.INFO, "View Logs files here: " + Constants.LOG_FOLDER_PATH);

        openResults();

    } // end onExecutionFinish

    /**
     * Opens the Masterthought report and html log file after execution has finished.
     */
    private void openResults() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions co = new ChromeOptions();
        co.addArguments("--remote-allow-origins=*");

        ChromeDriver driver = new ChromeDriver(co);

        driver.get(Constants.LOG_FOLDER_PATH + "execution-log.html");
        CommonMethods.pauseForSeconds(1);

        driver.manage().window().maximize();

        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(Constants.MASTERTHOUGHT_REPORT_PATH + "cucumber-html-reports\\overview-features.html");

    } // end openResults

} // end TestNGListener.java