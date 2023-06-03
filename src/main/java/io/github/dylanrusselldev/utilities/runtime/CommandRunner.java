/*
 * Filename: CommandRunner.java
 * Author: Dylan Russell
 * Purpose: Methods that allow commands to be run via the Windows Command Prompt
 */

package io.github.dylanrusselldev.utilities.runtime;

import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.LoggerClass;
import org.slf4j.event.Level;

public class CommandRunner {

    private static final LoggerClass LOGGER = new LoggerClass(CommandRunner.class);

    /**
     * Execute a command on the Windows Command Prompt.
     *
     * @param cmd the command to execute
     */
    public static void executeCommand(String cmd) {

        try {
            Runtime.getRuntime().exec(cmd);
            CommonMethods.pauseForSeconds(2);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error. Could not run the following command: " + cmd, e);
        } // end try-catch

    } // end executeCommand()

    /**
     * End the WebDriver .exe via the command line.
     */
    public static void endDriverExe() {

        String cmd = "taskkill /F /IM WEBDRIVEREXE";

        switch (RuntimeInfo.getBrowserName()) {
            case "chrome":
                cmd = cmd.replace("WEBDRIVEREXE", "chromedriver.exe");
                break;

            case "edge":
                cmd = cmd.replace("WEBDRIVEREXE", "edgedriver.exe");
                break;

            case "firefox":
                cmd = cmd.replace("WEBDRIVEREXE", "geckodriver.exe");
                break;

            case "ie":
                cmd = cmd.replace("WEBDRIVEREXE", "iedriverserver.exe");
                break;

            default:
                LOGGER.logAndFail("Browser was not defined properly");
                break;

        } // end switch statement

        LOGGER.log(Level.INFO, "Attempting to end WebDriver exe...");

        executeCommand(cmd);

    } // end endDriverExe()

} // end CommandRunner()
