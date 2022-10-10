/*
 * Filename: BrowserPreferences.java
 * Purpose: Sets browser preferences in the webdrivers
 */

package utilities.browsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import utilities.core.Hooks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BrowserPreferences {

    public static void chromePrefs(ChromeOptions co) throws IOException {

        // Disables the PDF viewer in Chrome to enable file downloads
        HashMap<String, Object> chromeMap = new HashMap<>();
        chromeMap.put("plugins.plugins_disabled", new String[] {"Chrome PDF Viewer"});
        chromeMap.put("plugins.always_open_pdf_externally", true);
        chromeMap.put("download.default_directory", System.getProperty("user.home") + "\\Downloads");
        co.setExperimentalOption("prefs", chromeMap);

        // Disable unnecessary console logging from Chrome
        System.setProperty("webdriver.chrome.args", "--disable-logging");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        // Add Chrome Options
        co.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        co.addArguments("no-sandbox", "start-maximized", "disable-dev-shm-usage", "enable-automation");

        // Enable Headless execution if -DHeadless is set to true
        if (Hooks.headless.equalsIgnoreCase("true")) {
            co.addArguments("headless", "window-size=1920,1080", "disable-extensions",
                    "disable-gpu", "dns-prefetch-disable", "hide-scrollbars");
            co.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            enableHeadlessDownloads(co);
        } else {
            Hooks.driver.set(new ChromeDriver(co));
        } // end if-else

    } // end chromePrefs()

    public static void edgePrefs(EdgeOptions edgeOpt) {

        // Disable unnecessary console logging from Edge
        System.setProperty("webdriver.edge.args", "--disable-logging");
        System.setProperty("webdriver.edge.silentOutput", "true");

        // Set the default download directory
        edgeOpt.setCapability("download.default_directory", System.getProperty("user.home") + "\\downloads");

        // Add Edge Options
        edgeOpt.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        edgeOpt.addArguments("no-sandbox", "start-maximized", "disable-dev-shm-usage", "enable-automation");

        // Enable Headless execution if -DHeadless is set to true
        if (Hooks.headless.equalsIgnoreCase("true")) {
            edgeOpt.addArguments("headless", "window-size=1920,1080", "disable-extensions",
                    "disable-gpu", "dns-prefetch-disable", "hide-scrollbars");
            edgeOpt.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        } else {
            Hooks.driver.set(new EdgeDriver(edgeOpt));
        } // end if else

    } // end edgePrefs()

    // By default, chrome does not support the downloading of files in headless mode
    // This enables that feature by sending the browser a http post request
    public static void enableHeadlessDownloads(ChromeOptions cOptions) throws IOException {
        ChromeDriverService ds = ChromeDriverService.createDefaultService();
        Hooks.driver.set(new ChromeDriver(ds, cOptions));

        Map<String, Object> commandParams = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        commandParams.put("cmd", "Page.setDownloadBehavior");
        params.put("behavior", "allow");
        params.put("downloadPath", System.getProperty("user.home") + "\\Downloads");
        commandParams.put("params", params);

        ObjectMapper objMapper = new ObjectMapper();

        String cmd = objMapper.writeValueAsString(commandParams);
        String sendCmd = ds.getUrl().toString() + "/session/" + ((RemoteWebDriver) Hooks.driver.get()).getSessionId() + "/chromium/send_command";
        HttpPost request = new HttpPost(sendCmd);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(cmd));

        HttpClient httpClient = HttpClientBuilder.create().build();
        httpClient.execute(request);
    } // end enableHeadlessDownloads()

} // end BrowserPreferences.java
