/*
 * Filename: DevToolsListener.java
 * Purpose: Creates a DevTools listener that will output network errors that do not return with a status code of 200.
 */

package utilities.helpers;

import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;

import java.util.ArrayList;

import static org.testng.Assert.fail;

public class DevToolsListener {

    public static final ArrayList<String> devtoolErrors = new ArrayList<>();

    public static void startDevToolsListener(WebDriver driver) {

        DevTools dt = ((ChromeDriver) driver ).getDevTools();

        // Attempt to start the DevTools listener
        try {
            dt.createSessionIfThereIsNotOne();
            dt.send(new Command<>("Network.enable", ImmutableMap.of()));
        } catch (Exception e) {
            fail("Could not start DevTools listener. Error Message: " + e.getMessage() + "\n");
        } // end try/catch block

        // Attempt to gather DevTools information
        try {

            dt.addListener(Network.responseReceived(), receive -> {
                String strStatus =
                receive
                .getResponse()
                .getStatus()
                .toString();

                // If a network response does not have 200 as the status code, add the info to the ArrayList
                if (!strStatus.equals("200")) {

                    devtoolErrors.add("DevTools error URL: "
                    + receive
                    .getResponse()
                    .getUrl()
                    .replace("https://", "") + "\n"
                    + "Status: "+ receive.getResponse().getStatus() + "\n"
                    + "Error: " + receive.getResponse().getStatusText() + "\n");

                } // end if

            });

        } catch (Exception e) {
            fail("Could not gather information from DevTools. Error Message: " + e.getMessage() + "\n");
        } // end try/catch block

    } // end startDevToolsListener()

} // end DevToolsListener.java
