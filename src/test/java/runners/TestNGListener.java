/*
 * Filename: TestNGListener.java
 * Author: Dylan Russell
 * Purpose: Methods that determine what to do before and after test execution
 */

package runners;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.core.Hooks;
import io.github.dylanrusselldev.utilities.core.LoggerClass;
import io.github.dylanrusselldev.utilities.core.MasterthoughtReport;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.IExecutionListener;

import java.io.IOException;

public class TestNGListener implements IExecutionListener {

    private static final LoggerClass LOGGER = new LoggerClass(TestNGListener.class);

    /**
     * Code that executes before all tests have started
     */
    public void onExecutionStart() {
        LOGGER.info("*** TEST EXECUTION STARTED ***");
    } // end onExecutionStart

    /**
     * Code that executes after all tests have finished
     */
    public void onExecutionFinish() {
        LOGGER.info("*** All scenarios have been run. Now generating the report ***");

        MasterthoughtReport.GenerateTestReport();

        String cmd = "taskkill /F /IM WEBDRIVEREXE";

        switch (Hooks.browser) {
            case "chrome":
                cmd = cmd.replace("WEBDRIVEREXE", "chromedriver.exe");
                break;

            case "edge":
                cmd = cmd.replace("WEBDRIVEREXE", "edgedriver.exe");
                break;
        } // end switch statement

        LOGGER.info("Attempting to end WebDriver exe...");

        try {
            Runtime.getRuntime().exec(cmd);
            CommonMethods.pauseForSeconds(2);
        } catch (IOException e) {
            LOGGER.warn("Could not end WebDriver instance with command: " + cmd, e);
        } // end try-catch

        CommonMethods.pauseForSeconds(1);

        LOGGER.info("\n***TEST EXECUTION FINISHED ***\n");
        LOGGER.info("View Report here: " + Constants.MASTERTHOUGHT_REPORT_PATH + "\n");
        LOGGER.info("View Logs files here: " + Constants.LOG_FOLDER_PATH + "\n");

        openResults();

    } // end onExecutionFinish

    /**
     * Opens the Masterthought report and html log file after execution has finished
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