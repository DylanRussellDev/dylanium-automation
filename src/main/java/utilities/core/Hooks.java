/*
 * Filename: Hooks.java
 * Author: Dylan Russell
 * Purpose: Setup options before the tests begin to execute
 */

package utilities.core;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.Capabilities;
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
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.browsers.BrowserPreferences;
import utilities.core.CommonMethods;

public class Hooks {
	
	public static WebDriver driver;
	public static Scenario scenario;
	private final ChromeOptions chromeOpt = new ChromeOptions();
	private final EdgeOptions edgeOpt = new EdgeOptions();
	public static Capabilities cap;
			
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

		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));
		driver.manage().window().maximize();
		cap = ( (RemoteWebDriver) getDriver()).getCapabilities();
		scenario.log("Executing on: " + CommonMethods.browserInfo(cap));
	} // end setup
	
	public static WebDriver getDriver() {
		return driver;
	} // end getWebdriver
	
	@After
	public void afterScenario() throws IOException {
		if (scenario.isFailed()) {
			CommonMethods.screenshot(driver, "Error Screenshot");

			if (!CommonMethods.devtoolErrors.isEmpty()) {
				Set<String> set = new HashSet<>(CommonMethods.devtoolErrors);
				CommonMethods.devtoolErrors.clear();
				CommonMethods.devtoolErrors.addAll(set);
				scenario.log(CommonMethods.devtoolErrors.toString());
			} // end inner if

		} // end outer if
		driver.quit();
	} // end afterScenario

}