/*
 * Filename: Hooks.java
 * Author: Dylan Russell
 * Purpose: Based off VM arguments given in the TestNG run config, 
 * 			launch the browser with the requested options
 */

package utilities.core;

import java.io.IOException;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.CapabilityType;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.browsers.BrowserPreferences;
import utilities.core.CommonMethods;

public class Hooks {
	
	public static WebDriver driver;
	public static Scenario scenario;
	private final ChromeOptions chromeOpt = new ChromeOptions();
	private final EdgeOptions edgeOpt = new EdgeOptions();
			
	@Before
	public void start(Scenario scenObj) throws IOException {
		scenario = scenObj;
		
		switch (System.getProperty("Browser").toLowerCase()) {
		
		case "chrome":
			WebDriverManager.chromedriver().setup();
			BrowserPreferences.chromePrefs(chromeOpt);
			break;
			
		case "chrome beta":
			System.setProperty("webdriver.chrome.driver", ".//WebDrivers//chromedriver-beta.exe");
			chromeOpt.setBinary("C:\\Program Files\\Google\\Chrome Beta\\Application\\chrome.exe");
			BrowserPreferences.chromePrefs(chromeOpt);
			break;
			
		case "chrome dev":
			System.setProperty("webdriver.chrome.driver", ".//WebDrivers//chromedriver-dev.exe");
			chromeOpt.setBinary("C:\\Program Files\\Google\\Chrome Dev\\Application\\chrome.exe");
			BrowserPreferences.chromePrefs(chromeOpt);
			break;
			
		case "edge":
			WebDriverManager.edgedriver().setup();
			BrowserPreferences.edgePrefs(edgeOpt);
			break;
		
		} // end switch
		
		driver.manage().window().maximize();
	} // end setup
	
	public static WebDriver getDriver() {
		return driver;
	} // end getWebdriver
	
	@After
	public void afterScenario() throws IOException {
		if (scenario.isFailed()) {
			CommonMethods.screenshot(driver, "Error Screenshot");
		}
		driver.quit();
	} // end afterScenario

}