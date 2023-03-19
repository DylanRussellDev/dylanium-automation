/*
 * Filename: DevToolsListener.java
 * Purpose: Creates a DevTools listener that will output network errors that do not return with a status code of 200.
 */

package io.github.dylanrusselldev.utilities.helpers;

import com.google.common.collect.ImmutableMap;
import io.github.dylanrusselldev.utilities.core.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v109.network.Network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DevToolsListener {

    public static ArrayList<String> devtoolErrors = new ArrayList<>();

    public static void startDevToolsListener(WebDriver driver) {

        DevTools dt = ((ChromeDriver) driver).getDevTools();
        dt.createSessionIfThereIsNotOne();
        dt.send(new Command<>("Network.enable", ImmutableMap.of()));

        dt.addListener(Network.responseReceived(), receive -> {
            Integer statusCode = receive.getResponse().getStatus();

            // If a network response has a status code >= 400, add the info to the ArrayList
            if (statusCode >= 400) {

                devtoolErrors.add("DevTools error found.\n" +
                        "URL: " + receive.getResponse().getUrl().replace("https://", "") + "\n" +
                        "Status: " + receive.getResponse().getStatus() + "\n" +
                        "Error: " + receive.getResponse().getStatusText());

            } // end if


        });

    } // end startDevToolsListener()

    // Print DevTools errors
    public static void logDevToolErrors() {
        if (!devtoolErrors.isEmpty()) {

            Set<String> set = new HashSet<>(devtoolErrors);

            for (String s : set) {
                Hooks.scenario.get().log(s);
            } // end for

        } // end if

    } // end logDevToolErrors()

} // end DevToolsListener.java
