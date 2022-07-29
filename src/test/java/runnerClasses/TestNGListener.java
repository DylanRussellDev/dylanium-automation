/*
 * Filename: TestNGListener.java
 * Author: Dylan Russell
 * Purpose: Handles what the developer sees in the console when execution starts and finishes.
 * 			This file calls the GenerateTestNGReport method.
 */

package runnerClasses;

import java.io.IOException;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.IExecutionListener;

import utilities.core.CommonMethods;
import utilities.core.GenerateReport;

public class TestNGListener implements IExecutionListener {

	public void onExecutionStart() {
		System.out.println("************ TEST EXECUTION STARTED ************\n");
	} // end onExecutionStart()
	
	public void onExecutionFinish() {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		} catch (IOException e) {
			System.out.println("Could not kill chromedriver");
		} // end try-catch

		System.out.println("GENERATING THE REPORT...\n");
		GenerateReport.GenerateTestNGReport();

		CommonMethods.pauseForSeconds(1);
		openTestReport();

		System.out.println("************ TEST EXECUTION FINISHED ************\n");

	} // end onExecutionFinish()

	public static void openTestReport() {
		String strFile = System.getProperty("user.dir") + "\\target\\~REPORTS\\cucumber-html-reports\\overview-features.html";
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get(strFile);
		driver.manage().window().maximize();
	}
	
} // end TestNGListener()