/*
 * Filename: Hooks.java
 * Purpose: Setup Before and After options for test execution.
 * 			Methods are called from the BrowserPreferences.java file
 * 			to help launch the drivers with the correct preferences.
 */

package utilities.core;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.browsers.BrowserPreferences;
import utilities.helpers.DevToolsListener;

public class Hooks {
	
	public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public static ThreadLocal<Scenario> scenario = new ThreadLocal<>();

	private final ChromeOptions chromeOpt = new ChromeOptions();
	private final EdgeOptions edgeOpt = new EdgeOptions();
	public static Capabilities cap;
			
	@Before
	public void start(Scenario scenObj) throws IOException {
		scenario.set(scenObj);
		
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

		driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));
		driver.get().manage().window().maximize();
		cap = ( (RemoteWebDriver) getDriver()).getCapabilities();
		scenario.get().log("Executing on: " + CommonMethods.browserInfo(cap));
	} // end setup
	
	public static WebDriver getDriver() {
		return driver.get();
	} // end getWebdriver
	
	@After
	public void afterScenario() throws IOException {
		if (scenario.get().isFailed()) {
			CommonMethods.screenshot(driver.get(), "Error Screenshot");

			if (!DevToolsListener.devtoolErrors.isEmpty()) {
				Set<String> set = new HashSet<>(DevToolsListener.devtoolErrors);
				DevToolsListener.devtoolErrors.clear();
				DevToolsListener.devtoolErrors.addAll(set);
				scenario.get().log(DevToolsListener.devtoolErrors.toString());
			} // end inner if

		} // end outer if
		driver.get().quit();
	} // end afterScenario

} // end Hooks.java
