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
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.event.Level;
import org.testng.IExecutionListener;

import java.io.IOException;

public class TestNGListener implements IExecutionListener {

    private static final LoggerClass LOGGER = new LoggerClass(TestNGListener.class);

    /**
     * Code that executes before all tests have started
     */
    public void onExecutionStart() {
        LOGGER.sendLog(Level.INFO, "*** TEST EXECUTION STARTED ***");
    } // end onExecutionStart

    /**
     * Code that executes after all tests have finished
     */
    public void onExecutionFinish() {
        LOGGER.sendLog(Level.INFO, "*** All scenarios have been run. Now generating the report ***");

        MasterthoughtReport.GenerateTestReport();

        String cmd = "taskkill /F /IM WEBDRIVEREXE";

        switch (RuntimeInfo.getBrowserName()) {
            case "chrome":
                cmd = cmd.replace("WEBDRIVEREXE", "chromedriver.exe");
                break;

            case "edge":
                cmd = cmd.replace("WEBDRIVEREXE", "edgedriver.exe");
                break;

            case "firefox":
                cmd = cmd.replace("WEBDRIVEREXE", "geckodriver.exe");
                break;

            case "ie":
                cmd = cmd.replace("WEBDRIVEREXE", "iedriverserver.exe");
                break;
        } // end switch statement

        LOGGER.sendLog(Level.INFO, "Attempting to end WebDriver exe...");

        try {
            Runtime.getRuntime().exec(cmd);
            CommonMethods.pauseForSeconds(2);
        } catch (IOException e) {
            LOGGER.sendLog(Level.WARN, "Could not end WebDriver instance with command: " + cmd, e);
        } // end try-catch

        CommonMethods.pauseForSeconds(1);

        LOGGER.sendLog(Level.INFO, "*** TEST EXECUTION FINISHED ***");
        LOGGER.sendLog(Level.INFO, "View Report here: " + Constants.MASTERTHOUGHT_REPORT_PATH);
        LOGGER.sendLog(Level.INFO, "View Logs files here: " + Constants.LOG_FOLDER_PATH);

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