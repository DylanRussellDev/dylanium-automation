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

    private Logger logger;

    public LoggerClass(Class<?> className) {
        this.logger = LoggerFactory.getLogger(className);
    } // end constructor

    public void sendLog(Level level, String msg) {

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

    public void sendLog(Level level, String msg, Throwable t) {

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

    public void logAndFail(Level level, String msg) {
        sendLog(level, msg);
        fail(msg + "\n");
    }

    public void logAndFail(Level level, String msg, Throwable t) {
        sendLog(level, msg, t);
        fail(msg + "\n");
    }

    public void logCucumberReport(Level level, String msg) {
        sendLog(level, msg);
        Hooks.scenario.get().log(msg);
    }

} // end LoggerClass
