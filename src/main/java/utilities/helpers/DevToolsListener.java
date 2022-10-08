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

public class DevToolsListener {

    public static final ArrayList<String> devtoolErrors = new ArrayList<>();

    /**
     * Starts a DevTools listener. Anytime a failure is captured during execution,
     * it is added to an Array list to output on the report.
     *
     * @param driver 		WebDriver
     */
    public static void startDevToolsListener(WebDriver driver) {

        try {

            DevTools dt = ((ChromeDriver) driver ).getDevTools();
            dt.createSessionIfThereIsNotOne();
            dt.send(new Command<>("Network.enable", ImmutableMap.of()));

            dt.addListener(Network.responseReceived(), receive -> {
                String strStatus = receive
                        .getResponse()
                        .getStatus()
                        .toString();

                if (!strStatus.equals("200")) {

                    devtoolErrors.add("DevTools error URL: " + receive
                            .getResponse()
                            .getUrl()
                            .replace("https://", "") + "\n"
                            + "Status: "+ receive.getResponse().getStatus() + ", Error: " + receive.getResponse().getStatusText() + "\n");

                } // end if

            });

        } catch (Exception e) {

            System.out.println("Could not start DevTools listener\n");

        } // end try catch

    } // end startDevToolsListener()

} // end DevToolsListener.java
