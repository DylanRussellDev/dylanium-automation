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
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * Filename: BrowserPreferences.java
 * Author: Dylan Russell
 * Purpose: Sets browser preferences for the webdrivers
 */
public class BrowserPreferences {

    /**
     * Setup the preferred options for the ChromeDriver.
     *
     * @return the Chrome Options
     */
    public static ChromeOptions chromePrefs() {

        // Disable unneccessary log messages from Selenium and Chrome
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.WARNING);
        System.setProperty("webdriver.chrome.args", "disable-logging");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        // Disables the PDF viewer in Chrome to automatically download pdf files
        HashMap<String, Object> chromeMap = new HashMap<>();
        chromeMap.put("plugins.plugins_disabled", new String[]{"Chrome PDF Viewer"});
        chromeMap.put("plugins.always_open_pdf_externally", true);
        chromeMap.put("download.default_directory", Constants.TARGET_FILE_DOWNLOADS + Hooks.getScenario().getName());

        // Add Chrome Options
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", chromeMap);
        chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        chromeOptions.addArguments(
                "no-sandbox",
                "disable-dev-shm-usage",
                "enable-automation",
                "disable-gpu",
                "dns-prefetch-disable",
                "disable-extensions",
                "ignore-certificate-errors",
                "ignore-urlfetcher-cert-requests");

        if (RuntimeInfo.isHeadless()) {
            chromeOptions.addArguments("headless=new", "window-size=1920,1080");
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        }

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
        edgeMap.put("plugins.plugins_disabled", new String[]{"Chromium PDF Viewer"});
        edgeMap.put("plugins.always_open_pdf_externally", true);
        edgeMap.put("download.default_directory", Constants.DOWNLOAD_DIRECTORY);

        // Add Edge Options
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setExperimentalOption("prefs", edgeMap);
        edgeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        edgeOptions.addArguments(
                "no-sandbox",
                "disable-dev-shm-usage",
                "enable-automation",
                "disable-gpu",
                "dns-prefetch-disable",
                "disable-extensions",
                "ignore-certificate-errors",
                "ignore-urlfetcher-cert-requests");

        if (RuntimeInfo.isHeadless()) {
            edgeOptions.addArguments("headless=new", "window-size=1920,1080");
            edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        }

        return edgeOptions;

    }

    /**
     * Enables the functionality of downloading files in headless mode by sending a command to the browser.
     */
    public static void overrideDownloadDirectory() throws IOException {

        String sendCmd = "";

        if (RuntimeInfo.getBrowserName().contains("chrome")) {

            ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
            Hooks.setDriver(new ChromeDriver(chromeDriverService, chromePrefs()));
            sendCmd = chromeDriverService.getUrl().toString()
                    + "/session/" + ((RemoteWebDriver) Hooks.getDriver()).getSessionId() + "/chromium/send_command";

        } else if (RuntimeInfo.getBrowserName().contains("edge")) {

            EdgeDriverService edgeDriverService = EdgeDriverService.createDefaultService();
            Hooks.setDriver(new EdgeDriver(edgeDriverService, edgePrefs()));
            sendCmd = edgeDriverService.getUrl().toString()
                    + "/session/" + ((RemoteWebDriver) Hooks.getDriver()).getSessionId() + "/chromium/send_command";

        }

        Map<String, Object> commandParams = new HashMap<>();
        commandParams.put("cmd", "Page.setDownloadBehavior");

        Map<String, Object> params = new HashMap<>();
        params.put("behavior", "allow");
        params.put("downloadPath", Constants.TARGET_FILE_DOWNLOADS + Hooks.getScenario().getName());
        commandParams.put("params", params);

        ObjectMapper objectMapper = new ObjectMapper();
        String cmd = objectMapper.writeValueAsString(commandParams);


        HttpPost httpPost = new HttpPost(sendCmd);
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(new StringEntity(cmd));

        HttpClient httpClient = HttpClientBuilder.create().build();
        httpClient.execute(httpPost);

    }

}
