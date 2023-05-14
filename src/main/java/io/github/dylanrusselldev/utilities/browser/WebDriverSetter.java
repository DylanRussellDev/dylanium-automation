/*
 * Filename: WebDriverSetter.java
 * Author: Dylan Russell
 * Purpose: Sets the WebDriver type based on the argument in the -DBrowser maven goal
 */

package io.github.dylanrusselldev.utilities.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.core.Hooks;
import io.github.dylanrusselldev.utilities.core.LoggerClass;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.IOException;
import java.time.Duration;

public class WebDriverSetter {

    private static final ChromeOptions co = new ChromeOptions();
    private static final EdgeOptions eo = new EdgeOptions();
    private static final LoggerClass LOGGER = new LoggerClass(WebDriverSetter.class);

    /**
     * Set the preferred browser for execution based of the -DBrowser Maven argument.
     *
     * @throws IOException
     */
    public static void setDriver() throws IOException {

        switch (RuntimeInfo.getBrowserName()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                Hooks.setDriver(new ChromeDriver(BrowserPreferences.chromePrefs(co)));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                Hooks.setDriver(new EdgeDriver(BrowserPreferences.edgePrefs(eo)));
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                Hooks.setDriver(new FirefoxDriver());
                break;

            case "ie":
                WebDriverManager.iedriver().setup();
                Hooks.setDriver(new InternetExplorerDriver());
                break;

            default:
                LOGGER.logAndFail("Browser was not defined properly");
                break;

        } // end switch

        // Set the page timeout
        Hooks.getDriver()
                .manage()
                .timeouts()
                .pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));

    } // end setDriver()

} // end WebDriverSetter.java
