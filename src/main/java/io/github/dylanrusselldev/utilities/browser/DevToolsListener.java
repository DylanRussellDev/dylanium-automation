/*
 * Filename: DevToolsListener.java
 * Author: Dylan Russell
 * Purpose: Creates a DevTools listener that will output network errors that do not return with a status code of 200.
 */

package io.github.dylanrusselldev.utilities.browser;

import com.google.common.collect.ImmutableMap;
import io.github.dylanrusselldev.utilities.core.LoggerClass;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v109.network.Network;
import org.openqa.selenium.edge.EdgeDriver;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DevToolsListener {

    private static final ArrayList<String> devtoolErrors = new ArrayList<>();
    private static final LoggerClass LOGGER = new LoggerClass(DevToolsListener.class);
    private final DevTools devTools;

    public DevToolsListener(WebDriver driver) {

        if (RuntimeInfo.getBrowserName().contains("chrome")) {
            devTools = ((ChromeDriver) driver).getDevTools();
        } else if (RuntimeInfo.getBrowserName().contains("edge")) {
            devTools = ((EdgeDriver) driver).getDevTools();
        } else {
            devTools = null;
        } // end if statement

        try {
            devTools.createSessionIfThereIsNotOne();
            devTools.send(new Command<>("Network.enable", ImmutableMap.of()));
            LOGGER.log(Level.INFO, "Started the DevTools Listener");
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Could not create a DevTools session", e);
        } // end try-catch

    } // end constructor

    /**
     * Start a DevTools listener.
     */
    public void startDevToolsListener() {

        try {

            LOGGER.log(Level.INFO, "Started the DevTools Listener");

            devTools.addListener(Network.responseReceived(), receive -> {
                Integer statusCode = receive.getResponse().getStatus();

                // If a network response has a status code >= 400, add the info to the ArrayList
                if (statusCode >= 400) {

                    devtoolErrors.add("DevTools error found.\n" +
                            "URL: " + receive.getResponse().getUrl().replace("https://", "") + "\n" +
                            "Status: " + receive.getResponse().getStatus() + "\n" +
                            "Error: " + receive.getResponse().getStatusText());

                } // end if

            }); // end addListener
        } catch (Exception e) {
            LOGGER.log(Level.WARN, "Could not create a DevTools session", e);
        } // end try-catch

    } // end startDevToolsListener()

    /**
     * Print the captured DevTools errors using the scenario object.
     */
    public static void logDevToolErrors() {
        if (!devtoolErrors.isEmpty()) {

            Set<String> set = new HashSet<>(devtoolErrors);

            for (String s : set) {
                LOGGER.logCucumberReport(s);
            } // end for

            devtoolErrors.clear();
        } // end if

    } // end logDevToolErrors()

} // end DevToolsListener.java
