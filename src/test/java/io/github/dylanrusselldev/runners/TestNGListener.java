/*
 * Filename: TestNGListener.java
 * Purpose: This class helps with outputting clear messages
 * 			to the console for execution status.
 */

package io.github.dylanrusselldev.runners;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.browsers.BrowserPreferences;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Hooks;
import io.github.dylanrusselldev.utilities.core.MasterthoughtReport;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.IExecutionListener;

import java.io.IOException;

public class TestNGListener implements IExecutionListener {

    public void onExecutionStart() {
        System.out.println("\n************ TEST EXECUTION STARTED ************");
    } // end onExecutionStart

    public void onExecutionFinish() {
        System.out.println("\nGENERATING THE REPORT...\n");
        MasterthoughtReport.GenerateTestReport();

        String cmd = "taskkill /F /IM WEBDRIVEREXE";

        switch (Hooks.browser) {
            case "chrome":
                cmd = cmd.replace("WEBDRIVEREXE", "chromedriver.exe");
                break;

            case "chrome beta":
                cmd = cmd.replace("WEBDRIVEREXE", "chromedriver-beta.exe");
                break;

            case "chrome dev":
                cmd = cmd.replace("WEBDRIVEREXE", "chromedriver-dev.exe");
                break;

            case "edge":
                cmd = cmd.replace("WEBDRIVEREXE", "edgedriver.exe");
                break;
        } // end switch statement

        // Attempt to end the chromedriver task
        try {
            Runtime.getRuntime().exec(cmd);
            CommonMethods.pauseForSeconds(2);
        } catch (IOException e) {
            System.out.println("Could not kill WebDriver instance with command: " + cmd);
        } // end try-catch

        CommonMethods.pauseForSeconds(1);

        try {
            openTestReport();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } // end try-catch

        System.out.println("\n************ TEST EXECUTION FINISHED ************\n");
    } // end onExecutionFinish

    /**
     * Opens the Masterthought report after execution is finished.
     */
    private void openTestReport() throws IOException {
        // The report path
        final String strFile = System.getProperty("user.dir") + "\\target\\~Masterthought-Report\\cucumber-html-reports\\overview-features.html";

        WebDriverManager.chromedriver().setup();

        ChromeOptions co = new ChromeOptions();
        ChromeDriver driver = new ChromeDriver(BrowserPreferences.chromePrefs(co));

        driver.get(strFile);
        driver.manage().window().maximize();
    } // end openTestReport

} // end TestNGListener.java