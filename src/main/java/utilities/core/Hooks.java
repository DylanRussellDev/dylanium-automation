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

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.browsers.WebDriverSetter;
import utilities.helpers.DevToolsListener;

public class Hooks {

    public static Capabilities cap;

    public static final String browser = System.getProperty("Browser").toLowerCase();
    public static final String headless = System.getProperty("Headless").toLowerCase();

	public static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public static final ThreadLocal<Scenario> scenario = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    } // end getWebdriver

	@Before
	public void start(Scenario scenObj) throws IOException {
		scenario.set(scenObj);

        WebDriverSetter.setDriver();

		driver.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));
		cap = ( (RemoteWebDriver) getDriver()).getCapabilities();
		scenario.get().log("Executing on: " + CommonMethods.browserInfo(cap));
	} // end setup
	
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
