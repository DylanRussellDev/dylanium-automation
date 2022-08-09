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
        System.setProperty("webdriver.chrome.args", "--disable-logging");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        co.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        co.setCapability("download.default_directory", System.getProperty("user.home") + "\\downloads");
        co.setCapability("download.prompt_for_download", true);

        co.addArguments("--no-sandbox", "--disable-dev-shm-usage", "enable-automation");

        if (System.getProperty("Headless").equalsIgnoreCase("true")) {
            co.addArguments("headless", "enable-automation", "window-size=1920,1080", "no-sandbox",
                    "disable-extensions", "disable-gpu", "dns-prefetch-disable", "hide-scrollbars");
            co.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            enableHeadlessDownloads(co);
        } else {
            Hooks.driver.set(new ChromeDriver(co));
        } // end if
    } // end chromePrefs

    public static void edgePrefs(EdgeOptions edgeOpt) {
        System.setProperty("webdriver.edge.args", "--disable-logging");
        System.setProperty("webdriver.edge.silentOutput", "true");

        edgeOpt.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        edgeOpt.setCapability("download.default_directory", System.getProperty("user.home") + "\\downloads");
        edgeOpt.setCapability("download.prompt_for_download", true);

        if (System.getProperty("Headless").equalsIgnoreCase("true")) {
            edgeOpt.addArguments("--headless", "enable-automation", "--window-size=1920,1080",
                    "--no-sandbox", "--disable-extensions", "--dns-prefetch-disable");
            edgeOpt.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        } // end if
        Hooks.driver.set(new EdgeDriver(edgeOpt));
    } // end edgePrefs

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

        HttpClient httpClient = HttpClientBuilder.create().build();
        String cmd = objMapper.writeValueAsString(commandParams);
        String sendCmd = ds.getUrl().toString() + "/session/" + ((RemoteWebDriver) Hooks.driver.get()).getSessionId() + "/chromium/send_command";
        HttpPost request = new HttpPost(sendCmd);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(cmd));
        httpClient.execute(request);
    } // end enableHeadlessDownloads

} // end BrowserPreferences.java
