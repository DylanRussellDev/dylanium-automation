package io.github.dylanrusselldev.utilities.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.fail;

/*
 * Filename: LoggerClass.java
 * Purpose: Provides methods that, once called, will log information to
 *          the console, the log file, and the Masterthought and Extent Reports
 */
public class LoggerClass {

    private Logger logger;

    public LoggerClass(Class<?> className){
        this.logger = LoggerFactory.getLogger(className);
    } // end constructor

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    public void errorAndFail(String msg) {
        logger.error(msg);
        fail(msg + "\n");
    }

    public void errorAndFail(String msg, Throwable t) {
        logger.error(msg, t);
        fail(msg + "\n");
    }

} // end LoggerClass
