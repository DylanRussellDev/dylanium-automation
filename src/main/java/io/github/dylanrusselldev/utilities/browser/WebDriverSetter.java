package io.github.dylanrusselldev.utilities.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.core.Hooks;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.IOException;
import java.time.Duration;

/*
 * Filename: WebDriverSetter.java
 * Author: Dylan Russell
 * Purpose: Sets the WebDriver type based on the argument in the -DBrowser maven goal
 */
public class WebDriverSetter {

    private static final LoggerClass LOGGER = new LoggerClass(WebDriverSetter.class);

    /**
     * Set the preferred browser for execution based of the -DBrowser Maven argument.
     */
    public static synchronized void setDriver() throws IOException {

        switch (RuntimeInfo.getBrowserName()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                Hooks.setDriver(new ChromeDriver(BrowserPreferences.chromePrefs()));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                Hooks.setDriver(new EdgeDriver(BrowserPreferences.edgePrefs()));
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

        }

        Hooks.getDriver().manage().window().maximize();
        Hooks.getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constants.TIMEOUT));

    }

}