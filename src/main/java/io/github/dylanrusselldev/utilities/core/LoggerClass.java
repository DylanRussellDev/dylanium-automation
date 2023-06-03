/*
 * Filename: LoggerClass.java
 * Author: Dylan Russell
 * Purpose: Provides methods that, once called, will log information to
 *          the console, a log file, an html file and the Masterthought report
 */

package io.github.dylanrusselldev.utilities.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import static org.testng.Assert.fail;

public class LoggerClass {

    private final Logger logger;

    public LoggerClass(Class<?> className) {
        this.logger = LoggerFactory.getLogger(className);
    } // end constructor

    /**
     * Logs a message to the html and log text files.
     *
     * @param level the severity of the log message
     * @param msg   the message to include in the log files
     */
    public void log(Level level, String msg) {

        switch (level) {

            case INFO:
                logger.info(msg);
                break;

            case WARN:
                logger.warn(msg);
                break;

            case ERROR:
                logger.error(msg);
                break;

            case DEBUG:
                logger.debug(msg);
                break;

            case TRACE:
                logger.trace(msg);
                break;

        } // end switch

    } // end method

    /**
     * Logs a message to the html and log text files that includes the Exception information.
     *
     * @param level the severity of the log message
     * @param msg   the message to include in the log files
     * @param t     the Exception
     */
    public void log(Level level, String msg, Throwable t) {

        switch (level) {

            case INFO:
                logger.info(msg, t);
                break;

            case WARN:
                logger.warn(msg, t);
                break;

            case ERROR:
                logger.error(msg, t);
                break;

            case DEBUG:
                logger.debug(msg, t);
                break;

            case TRACE:
                logger.trace(msg, t);
                break;

        } // end switch

    } // end method

    /**
     * Logs a user friendly error message on the cucumber reports using the Assert.fail method.
     * Also includes a custom log message in the log text and html file.
     *
     * @param msg the message to include in the log files
     */
    public void logAndFail(String msg) {
        log(Level.ERROR, msg);
        fail(msg + "\n");
    }

    /**
     * Logs a user friendly error message on the cucumber reports using the Assert.fail method.
     * Also includes a custom log message in the log text and html file along with the stack trace.
     *
     * @param msg the message to include in the log files
     * @param t   the Exception
     */
    public void logAndFail(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
        fail(msg + "\n");
    }

    /**
     * Logs a message only in the cucumber reports
     *
     * @param msg the message to include in the log files
     */
    public void logCucumberReport(String msg) {
        Hooks.getScenario().log(msg);
    }

} // end LoggerClass
