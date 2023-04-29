/*
 * Filename: ThreadSetter.java
 * Author: Dylan Russell
 * Purpose: Enables the number provided for the -DThreads maven goal to control
 *          how many browser instances will be launched while executing in parallel.
 */

package runners;

import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import org.testng.IAlterSuiteListener;
import org.testng.xml.XmlSuite;

import java.util.List;

public class ThreadSetter implements IAlterSuiteListener {

    @Override
    public void alter(List<XmlSuite> suites) {

        XmlSuite suite = suites.get(0);
        suite.setDataProviderThreadCount(RuntimeInfo.getThreads());
        suite.setThreadCount(RuntimeInfo.getThreads());

    }

}
