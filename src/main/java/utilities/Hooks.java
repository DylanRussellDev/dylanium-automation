/*
 * Filename: Hooks.java
 * Author: Dylan Russell
 * Purpose: Based off VM arguments given in the TestNG run config, 
 * 			launch the browser with the requested options
 */

package utilities;

import java.io.IOException;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hooks {
	
	public static WebDriver driver;
	public static Scenario scenario;
	private ChromeOptions chromeOpt = new ChromeOptions();
	private EdgeOptions edgeOpt = new EdgeOptions();
			
	@Before
	public void start(Scenario scenObj) {
		scenario = scenObj;
		
		switch (System.getProperty("Browser").toLowerCase()) {
		
		case "chrome":
			WebDriverManager.chromedriver().setup();
			chromeSetupOptions(chromeOpt);
			break;
			
		case "chrome beta":
			System.setProperty("webdriver.chrome.driver", ".//WebDrivers//chromedriver-beta.exe");
			chromeOpt.setBinary("C:\\Program Files\\Google\\Chrome Beta\\Application\\chrome.exe");
			chromeSetupOptions(chromeOpt);
			break;
			
		case "chrome dev":
			System.setProperty("webdriver.chrome.driver", ".//WebDrivers//chromedriver-dev.exe");
			chromeOpt.setBinary("C:\\Program Files\\Google\\Chrome Dev\\Application\\chrome.exe");
			chromeSetupOptions(chromeOpt);
			break;
			
		case "edge":
			WebDriverManager.edgedriver().setup();
			edgeSetupOptions(edgeOpt);
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
	
	private void chromeSetupOptions(ChromeOptions chromeOpt) {
		System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		
		chromeOpt.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		chromeOpt.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		chromeOpt.setCapability("download.default_directory", System.getProperty("user.home") + "\\downloads");
		chromeOpt.setCapability("download.prompt_for_download", true);
		
		chromeOpt.addArguments("--no-sandbox", "--disable-dev-shm-usage", "enable-automation");
		
		if (System.getProperty("Headless").equalsIgnoreCase("true")) {
			chromeOpt.addArguments("--headless", "--window-size=1920,1080",
					"--disable-extensions", "--dns-prefetch-disable");
			chromeOpt.setPageLoadStrategy(PageLoadStrategy.NORMAL);	
		} // end if
		
		driver = new ChromeDriver(chromeOpt);
	} // end chromeSetupOptions
	
	private void edgeSetupOptions(EdgeOptions edgeOpt) {
		System.setProperty("webdriver.edge.args", "--disable-logging");
		System.setProperty("webdriver.edge.silentOutput", "true");
		
		edgeOpt.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		edgeOpt.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		edgeOpt.setCapability("download.default_directory", System.getProperty("user.home") + "\\downloads");
		edgeOpt.setCapability("download.prompt_for_download", true);
		
		if (System.getProperty("Headless").equalsIgnoreCase("true")) {
			edgeOpt.addArguments("--headless", "enable-automation", "--window-size=1920,1080", 
					"--no-sandbox", "--disable-extensions", "--dns-prefetch-disable");
			edgeOpt.setPageLoadStrategy(PageLoadStrategy.NORMAL);	
		} // end if
		
		driver = new EdgeDriver(edgeOpt);
	} // end chromeSetupOptions
}