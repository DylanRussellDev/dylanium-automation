/*
 * Filename: RuntimeInfo.java
 * Author: Dylan Russell
 * Purpose: A class that houses methods can return information about the current
 *          suite being executed.
 */

package io.github.dylanrusselldev.utilities.runtime;

import io.github.dylanrusselldev.utilities.core.LoggerClass;
import org.openqa.selenium.Capabilities;
import org.slf4j.event.Level;

public class RuntimeInfo {

    private static final LoggerClass LOGGER = new LoggerClass(RuntimeInfo.class);

    public static String getBrowserName() {

        try {
            String browser = System.getProperty("Browser").toLowerCase();

            if (browser.equals("chrome") || browser.equals("edge") || browser.equals("firefox") || browser.equals("ie")) {
                return browser;
            } else {
                LOGGER.logAndFail(Level.ERROR, " Error. The -DBrowser property only accepts 'chrome', 'edge', 'firefox', or 'ie' as an argument");
                return null;
            } // end if else
        } catch (Exception e) {
            LOGGER.logAndFail(Level.ERROR, " The -DBrowser property was not initialized correctly", e);
            return null;
        } // end try catch

    } // end getBrowserName()

    /**
     * Returns the browser information for the reports
     *
     * @param threadCap The Capabilities information passed in from Hooks
     */
    public static String getBrowserVersion(Capabilities threadCap) {

        try {

            String str = threadCap.getBrowserName().equalsIgnoreCase("Msedge") ? "MS Edge" : threadCap.getBrowserName();
            String info = str + " " + threadCap.getBrowserVersion();
            return info.substring(0, 1).toUpperCase() + info.substring(1);

        } catch (Exception e) {

            LOGGER.sendLog(Level.WARN, "Could not find browser name and version the tests were executed on", e);
            return null;

        } // end try catch

    } // end getBrowserVersion()

    /**
     * Returns the OS the tests are executing on to include in the report
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
        } // end if-else statement

        return strOS;

    } // end getOSInfo()

    public static Integer getThreads() {
        int threads;
        final int MIN_THREADS = 1;
        final int MAX_THREADS = 4;

        try {

            threads = Integer.parseInt(System.getProperty("Threads"));

            if (threads <= MAX_THREADS && threads >= MIN_THREADS) {

                return threads;

            } else {

                LOGGER.logAndFail(Level.ERROR, " The -DThreads property argument must be greater than 0 and less than or equal to 4");
                return null;

            } // end if else

        } catch (Exception e) {

            LOGGER.logAndFail(Level.ERROR, " The -DThreads property was not initialized correctly", e);
            return null;

        } // end try catch

    } // end getThreads()

    public static boolean isHeadless() {

        try {

            String headless = System.getProperty("Headless").toLowerCase();

            if (headless.equals("true") || headless.equals("false")) {

                return Boolean.parseBoolean(headless);

            } else {

                LOGGER.logAndFail(Level.ERROR, " Error. The -DHeadless property only accepts 'true' or 'false' as an argument");
                return false;

            } // end if else

        } catch (Exception e) {

            LOGGER.logAndFail(Level.ERROR, " The -DHeadless property was not initialized correctly", e);
            return false;

        } // end try catch

    } // end isHeadless()


} // end RuntimeInfo.java
