package io.github.dylanrusselldev.utilities.browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.dylanrusselldev.utilities.core.Hooks;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.IOException;

/*
 * Filename: WebDriverSetter.java
 * Purpose: Sets the WebDriver type based on the argument in the -DBrowser maven goal
 */
public class WebDriverSetter {

    private static final ChromeOptions co = new ChromeOptions();
    private static final EdgeOptions eo = new EdgeOptions();

    public static void setDriver() throws IOException {

        switch (Hooks.browser) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                Hooks.driver.set(new ChromeDriver(BrowserPreferences.chromePrefs(co)));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                Hooks.driver.set(new EdgeDriver(BrowserPreferences.edgePrefs(eo)));
                break;

        } // end switch

    } // end setDriver()

} // end WebDriverSetter.java
