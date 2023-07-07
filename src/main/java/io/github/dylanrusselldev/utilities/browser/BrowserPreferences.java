/*
 * Filename: BrowserPreferences.java
 * Author: Dylan Russell
 * Purpose: Sets browser preferences for the webdrivers
 */

package io.github.dylanrusselldev.utilities.browser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.core.Hooks;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BrowserPreferences {

    /**
     * Set the preferred options to the ChromeDriver.
     *
     * @return the complete Chrome Options
     */
    public static ChromeOptions chromePrefs() throws IOException {

        // Disable unneccessary log messages from Selenium and Chrome
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.WARNING);
        System.setProperty("webdriver.chrome.args", "disable-logging");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        // Disables the PDF viewer in Chrome to enable file downloads
        HashMap<String, Object> chromeMap = new HashMap<>();
        chromeMap.put("plugins.plugins_disabled", new String[]{"Chrome PDF Viewer"});
        chromeMap.put("plugins.always_open_pdf_externally", true);
        chromeMap.put("download.default_directory", Constants.DOWNLOAD_DIRECTORY);

        // Add Chrome Options
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromeMap);
        chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        chromeOptions.addArguments(
                "remote-allow-origins=*",
                "no-sandbox",
                "start-maximized",
                "disable-dev-shm-usage",
                "enable-automation",
                "disable-gpu",
                "dns-prefetch-disable",
                "disable-extensions");

        // Enable Headless execution if -DHeadless is set to true
        if (RuntimeInfo.isHeadless()) {

            chromeOptions.addArguments(
                    "headless=new",
                    "window-size=1920,1080");

            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            enableHeadlessDownloads(chromeOptions);

        } // end if

        return chromeOptions;

    }

    /**
     * Set the preferred options to the EdgeDriver.
     *
     * @return the complete Edge Options
     */
    public static EdgeOptions edgePrefs() {

        // Disable unnecessary console logging from Selenium and Edge
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.WARNING);
        System.setProperty("webdriver.edge.args", "disable-logging");
        System.setProperty("webdriver.edge.silentOutput", "true");

        // Set the default download directory
        HashMap<String, Object> edgeMap = new HashMap<>();
        edgeMap.put("plugins.plugins_disabled", new String[]{"Edge PDF Viewer"});
        edgeMap.put("plugins.always_open_pdf_externally", true);
        edgeMap.put("download.default_directory", Constants.DOWNLOAD_DIRECTORY);

        // Add Edge Options
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setExperimentalOption("prefs", edgeMap);
        edgeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        edgeOptions.addArguments(
                "no-sandbox",
                "start-maximized",
                "disable-dev-shm-usage",
                "enable-automation",
                "disable-gpu",
                "dns-prefetch-disable",
                "disable-extensions");

        // Enable Headless execution if -DHeadless is set to true
        if (RuntimeInfo.isHeadless()) {

            edgeOptions.addArguments(
                    "headless=new",
                    "window-size=1920,1080");

            edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        } // end if

        return edgeOptions;

    }

    /**
     * Enables the functionality of downloading files in headless mode by sending a command to the browser.
     *
     * @param cOptions the Chrome Options object
     */
    public static void enableHeadlessDownloads(ChromeOptions cOptions) throws IOException {

        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        Hooks.setDriver(new ChromeDriver(chromeDriverService, cOptions));

        Map<String, Object> commandParams = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        commandParams.put("cmd", "Page.setDownloadBehavior");
        params.put("behavior", "allow");
        params.put("downloadPath", Constants.TARGET_FILE_DOWNLOADS + Hooks.getScenario().getName());
        commandParams.put("params", params);

        ObjectMapper objectMapper = new ObjectMapper();

        String cmd = objectMapper.writeValueAsString(commandParams);
        String sendCmd = chromeDriverService.getUrl().toString()
                + "/session/" + ((RemoteWebDriver) Hooks.getDriver()).getSessionId() + "/chromium/send_command";

        HttpPost httpPost = new HttpPost(sendCmd);
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(new StringEntity(cmd));

        HttpClient httpClient = HttpClientBuilder.create().build();
        httpClient.execute(httpPost);

    }

}
