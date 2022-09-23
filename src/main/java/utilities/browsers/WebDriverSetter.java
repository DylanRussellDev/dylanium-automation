/*
 * Filename: WebDriverSetter.java
 * Purpose: Sets the webdriver to the one desired based on the
 *          argument from the -DBrowser maven goal
 */

package utilities.browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import utilities.core.Hooks;

import java.io.IOException;

public class WebDriverSetter {

    private static final ChromeOptions co = new ChromeOptions();
    private static final EdgeOptions eo = new EdgeOptions();

    public static void setDriver() throws IOException {

        switch (Hooks.browser) {

            case "chrome":
                WebDriverManager.chromedriver().setup();
                BrowserPreferences.chromePrefs(co);
                break;

            case "chrome beta":
                System.setProperty("webdriver.chrome.driver", ".//WebDrivers//chromedriver-beta.exe");
                co.setBinary("C:\\Program Files\\Google\\Chrome Beta\\Application\\chrome.exe");
                BrowserPreferences.chromePrefs(co);
                break;

            case "chrome dev":
                System.setProperty("webdriver.chrome.driver", ".//WebDrivers//chromedriver-dev.exe");
                co.setBinary("C:\\Program Files\\Google\\Chrome Dev\\Application\\chrome.exe");
                BrowserPreferences.chromePrefs(co);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                BrowserPreferences.edgePrefs(eo);
                break;

        } // end switch

    } // end setDriver()

} // end WebDriverSetter.java
