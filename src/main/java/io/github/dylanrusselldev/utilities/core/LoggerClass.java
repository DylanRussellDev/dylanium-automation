/*
 * Filename: LoggerClass.java
 * Author: Dylan Russell
 * Purpose: Provides methods that, once called, will log information to
 *          the console, a log file, an html file and the Masterthought report
 */

package io.github.dylanrusselldev.utilities.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.fail;

public class LoggerClass {

    private final Logger logger;

    public LoggerClass(Class<?> className) {
        this.logger = LoggerFactory.getLogger(className);
    } // end constructor

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(String msg, Exception e) {
        logger.info(msg, e);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(String msg, Exception e) {
        logger.warn(msg, e);
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(String msg, Exception e) {
        logger.error(msg, e);
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void debug(String msg, Exception e) {
        logger.debug(msg, e);
    }

    public void trace(String msg) {
        logger.trace(msg);
    }

    public void trace(String msg, Exception e) {
        logger.trace(msg, e);
    }

    /**
     * Logs a user friendly error message on the cucumber reports using the Assert.fail method.
     * Also includes a custom log message in the log text and html file.
     *
     * @param msg the message to include in the log files
     */
    public void logAndFail(String msg) {
        error(msg);
        fail(msg + "\n");
    }

    /**
     * Logs a user friendly error message on the cucumber reports using the Assert.fail method.
     * Also includes a custom log message in the log text and html file along with the stack trace.
     *
     * @param msg the message to include in the log files
     * @param e   the Exception
     */
    public void logAndFail(String msg, Exception e) {
        error(msg, e);
        fail(msg + "\n");
    }

    /**
     * Logs a message only in the cucumber reports
     *
     * @param msg the message to include in the log files
     */
    public void logCucumberReport(String msg) {
        Hooks.getScenario().log(msg);
        info(msg);
    }

} // end LoggerClass
