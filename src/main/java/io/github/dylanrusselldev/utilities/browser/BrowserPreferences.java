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
     * @param  chromeOpt       Chrome Options object
     * @throws IOException
     */
    public static ChromeOptions chromePrefs(ChromeOptions chromeOpt) throws IOException {

        // Disables the PDF viewer in Chrome to enable file downloads
        HashMap<String, Object> chromeMap = new HashMap<>();
        chromeMap.put("plugins.plugins_disabled", new String[]{"Chrome PDF Viewer"});
        chromeMap.put("plugins.always_open_pdf_externally", true);
        chromeMap.put("download.default_directory", Constants.DOWNLOAD_DIRECTORY);
        chromeOpt.setExperimentalOption("prefs", chromeMap);

        // Disable unnecessary console logging from Chrome
        System.setProperty("webdriver.chrome.args", "--disable-logging");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        // Add Chrome Options
        chromeOpt.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        chromeOpt.addArguments(
                "--remote-allow-origins=*",
                "no-sandbox",
                "start-maximized",
                "disable-dev-shm-usage",
                "enable-automation",
                "disable-gpu",
                "dns-prefetch-disable",
                "disable-extensions");

        // Enable Headless execution if -DHeadless is set to true
        if (RuntimeInfo.isHeadless()) {

            chromeOpt.addArguments(
                    "--headless=new",
                    "window-size=1920,1080");

            chromeOpt.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            enableHeadlessDownloads(chromeOpt);

        } // end if

        return chromeOpt;

    } // end chromePrefs()

    /**
     * Set the preferred options to the EdgeDriver.
     *
     * @param  edgeOpt       Edge Options object
     * @throws IOException
     */
    public static EdgeOptions edgePrefs(EdgeOptions edgeOpt) {

        // Set the default download directory
        HashMap<String, Object> edgeMap = new HashMap<>();
        edgeMap.put("plugins.plugins_disabled", new String[]{"Edge PDF Viewer"});
        edgeMap.put("plugins.always_open_pdf_externally", true);
        edgeMap.put("download.default_directory", Constants.DOWNLOAD_DIRECTORY);
        edgeOpt.setExperimentalOption("prefs", edgeMap);

        // Disable unnecessary console logging from Browser
        System.setProperty("webdriver.edge.args", "--disable-logging");
        System.setProperty("webdriver.edge.silentOutput", "true");

        // Add Edge Options
        edgeOpt.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        edgeOpt.addArguments(
                "no-sandbox",
                "start-maximized",
                "disable-dev-shm-usage",
                "enable-automation",
                "disable-gpu",
                "dns-prefetch-disable",
                "disable-extensions");

        // Enable Headless execution if -DHeadless is set to true
        if (RuntimeInfo.isHeadless()) {

            edgeOpt.addArguments(
                    "--headless=new",
                    "window-size=1920,1080");

            edgeOpt.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        } // end if

        return edgeOpt;

    } // end edgePrefs()

    /**
     * Enables the functionality of downloading files in headless mode by sending a command to the browser.
     *
     * @param  cOptions      Chrome Options object
     * @throws IOException
     */
    public static void enableHeadlessDownloads(ChromeOptions cOptions) throws IOException {

        ChromeDriverService ds = ChromeDriverService.createDefaultService();
        Hooks.setDriver(new ChromeDriver(ds, cOptions));

        Map<String, Object> commandParams = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        commandParams.put("cmd", "Page.setDownloadBehavior");
        params.put("behavior", "allow");
        params.put("downloadPath", Constants.TARGET_FILE_DOWNLOADS + Hooks.getScenario().getName());
        commandParams.put("params", params);

        ObjectMapper objMapper = new ObjectMapper();

        String cmd = objMapper.writeValueAsString(commandParams);
        String sendCmd = ds.getUrl().toString() + "/session/" + ((RemoteWebDriver) Hooks.getDriver()).getSessionId() + "/chromium/send_command";

        HttpPost request = new HttpPost(sendCmd);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(cmd));

        HttpClient httpClient = HttpClientBuilder.create().build();
        httpClient.execute(request);

    } // end enableHeadlessDownloads()

} // end BrowserPreferences.java
