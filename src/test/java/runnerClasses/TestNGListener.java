/*
 * Filename: TestNGListener.java
 * Purpose: This class helps with outputting clear messages
 * 			to the console for execution status.
 * 			
 */

package runnerClasses;

import java.io.IOException;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.IExecutionListener;

import utilities.core.CommonMethods;
import utilities.core.GenerateReport;
import utilities.core.Hooks;

public class TestNGListener implements IExecutionListener {

	public void onExecutionStart() {
		System.out.println("************ TEST EXECUTION STARTED ************\n");
	} // end onExecutionStart
	
	public void onExecutionFinish() {
        System.out.println("GENERATING THE REPORT...\n");
        GenerateReport.GenerateTestNGReport();
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
        }

		// Attempt to end the chromedriver task
		try {
			Runtime.getRuntime().exec(cmd);
			CommonMethods.pauseForSeconds(2);
		} catch (IOException e) {
			System.out.println("Could not kill webdriver instance");
		} // end try-catch

        openTestReport();

		System.out.println("************ TEST EXECUTION FINISHED ************\n");
	} // end onExecutionFinish

	// Open the report after execution finishes
	public static void openTestReport() {
		String strFile = System.getProperty("user.dir") + "\\target\\~REPORT\\cucumber-html-reports\\overview-features.html";
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get(strFile);
		driver.manage().window().maximize();
	} // end openTestReport
	
} // end TestNGListener.java