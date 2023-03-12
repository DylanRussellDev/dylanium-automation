/*
 * Filename: ThreadSetter.java
 * Purpose: Enables the number provided for the -DThreads maven goal
 * 			to control how many browser instances will be launched while
 * 			executing in parallel.
 */

package io.github.dylanrusselldev.runners;

import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.util.List;

import static org.testng.Assert.fail;

public class ThreadSetter implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {

        try {

            int count = Integer.parseInt(System.getProperty("Threads"));
            int limit = 5;

            // Limit the amount of tests executed at the same time to prevent the system from using too many resources
            if (count > limit) {
                fail("To maintain system stability, please use a -DThreads value lower than " + limit);
            } // end if

            XmlSuite suite = suites.get(0);
            suite.setDataProviderThreadCount(count);
            suite.setThreadCount(count);

        } catch (Exception e) {

            fail("The -DThreads Maven goal was not set in the run configuration. Please rerun with the -DThreads goal.");

        } // end try-catch

    } // end setParallelInstances

}
