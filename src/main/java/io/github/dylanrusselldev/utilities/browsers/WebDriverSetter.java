/*
 * Filename: WebDriverSetter.java
 * Author: Dylan Russell
 * Purpose: Sets the WebDriver type based on the argument in the -DBrowser maven goal
 */

package io.github.dylanrusselldev.utilities.browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.core.Hooks;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.IOException;

public class WebDriverSetter {

    private static final ChromeOptions co = new ChromeOptions();
    private static final EdgeOptions eo = new EdgeOptions();

    /**
     * Set the preferred Browser for execution based of the -DBrowser Maven argument
     *
     * @throws IOException
     */
    public static void setDriver() throws IOException {

        switch (Hooks.browser.toLowerCase()) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                Hooks.driver.set(new ChromeDriver(BrowserPreferences.chromePrefs(co)));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                Hooks.driver.set(new EdgeDriver(BrowserPreferences.edgePrefs(eo)));
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                Hooks.driver.set(new FirefoxDriver());
                break;

            case "ie":
                WebDriverManager.iedriver().setup();
                Hooks.driver.set(new InternetExplorerDriver());
                break;

        } // end switch

    } // end setDriver()

} // end WebDriverSetter.java
