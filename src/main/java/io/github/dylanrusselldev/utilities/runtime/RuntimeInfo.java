package io.github.dylanrusselldev.utilities.runtime;

import io.github.dylanrusselldev.steps.Hooks;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import org.openqa.selenium.Capabilities;

import java.util.ArrayList;
import java.util.List;

/*
 * Filename: RuntimeInfo.java
 * Author: Dylan Russell
 * Purpose: Provides methods that can return information about the current suite being executed.
 */
public class RuntimeInfo {

    public static Capabilities capabilities;
    private static final LoggerClass LOGGER = new LoggerClass(RuntimeInfo.class);

    /**
     * Retrieve the Browser argument from the -DBrowser property.
     * This also checks to see if the -DBrowser property was initialized correctly.
     *
     * @return the browser name given in the -DBrowser property
     */
    public static String getBrowserName() {

        try {
            String browser = System.getProperty("Browser").toLowerCase();

            if (browser.equals("chrome") || browser.equals("edge") || browser.equals("firefox") || browser.equals("ie")) {
                return browser;
            } else {
                LOGGER.fail(" Error. The -DBrowser property only accepts 'chrome', 'edge', 'firefox', or 'ie' as an argument");
                return null;
            }

        } catch (Exception e) {
            LOGGER.fail(" The -DBrowser property was not initialized correctly", e);
            return null;
        }

    }

    private static void setCapabilities(Capabilities cap) {
        capabilities = cap;
    }

    /**
     * Retrieves the browser name and version that the scripts are executing on.
     *
     * @return the browser name and version
     */
    public static String getBrowserVersion(Capabilities cap) {
        setCapabilities(cap);

        try {
            String str = cap.getBrowserName().equalsIgnoreCase("Msedge") ? "MS Edge" : cap.getBrowserName();
            String info = str + " " + cap.getBrowserVersion();
            return info.substring(0, 1).toUpperCase() + info.substring(1);
        } catch (Exception e) {
            LOGGER.warn("Could not find browser name and version the tests were executed on", e);
            return null;
        }

    }

    /**
     * Identify the Operating System that the scripts are executing on.
     *
     * @return string OS as a string
     */
    public static String getOSInfo() {
        String os = System.getProperty("os.name").toLowerCase();
        String strOS;

        if (os.contains("win")) {
            strOS = "Windows";
        } else if (os.contains("nux") || os.contains("nix")) {
            strOS = "Linux";
        } else if (os.contains("mac")) {
            strOS = "Mac";
        } else if (os.contains("sunos")) {
            strOS = "Solaris";
        } else {
            strOS = "Other";
        }

        return strOS;
    }

    /**
     * Retrieve the number of threads given in the -DThreads property.
     * This also checks to see if the -DThreads property was initialized correctly.
     *
     * @return the number of threads
     */
    public static Integer getThreads() {
        int threads;
        final int MIN_THREADS = 1;
        final int MAX_THREADS = 4;

        try {
            threads = Integer.parseInt(System.getProperty("Threads"));

            if (threads <= MAX_THREADS && threads >= MIN_THREADS) {
                return threads;
            } else {
                LOGGER.fail(" The -DThreads property argument must be greater than 0 and less than or equal to 4");
                return null;
            }

        } catch (Exception e) {
            LOGGER.fail(" The -DThreads property was not initialized correctly", e);
            return null;
        }

    }

    /**
     * Returns a string of the most unique Cucumber tag associated with the current scenario
     */
    public static String getUniqueScenarioTag() {
        List<String> allTags = new ArrayList<>(Hooks.getScenario().getSourceTagNames());
        return allTags.get(allTags.size() - 1);
    }

    /**
     * Returns the value given from the -DHeadless property.
     * This also checks to see if the -DHeadless property was initialized correctly.
     *
     * @return is headless set to true or false
     */
    public static boolean isHeadless() {

        try {
            String headless = System.getProperty("Headless").toLowerCase();

            if (headless.equals("true") || headless.equals("false")) {
                return Boolean.parseBoolean(headless);
            } else {
                LOGGER.fail(" Error. The -DHeadless property only accepts 'true' or 'false' as an argument");
                return false;
            }

        } catch (Exception e) {
            LOGGER.fail(" The -DHeadless property was not initialized correctly", e);
            return false;
        }

    }

}
