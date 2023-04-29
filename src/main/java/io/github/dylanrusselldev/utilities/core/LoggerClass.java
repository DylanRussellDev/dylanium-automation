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

    }

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

    }

    public void logAndFail(String msg) {
        log(Level.ERROR, msg);
        fail(msg + "\n");
    }

    public void logAndFail(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
        fail(msg + "\n");
    }

    public void logCucumberReport(String msg) {
        Hooks.getScenario().log(msg);
    }

} // end LoggerClass
