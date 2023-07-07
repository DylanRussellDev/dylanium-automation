/*
 * Filename: Runner.java
 * Author: Dylan Russell
 * Purpose: The cucumber options for test execution.
 * 			If test execution is not working, check the correct
 * 			package names and folder locations are used.
 */

package runners;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.github.dylanrusselldev.utilities.core.Constants;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/java/features",                                           // Location of where the Feature File package is
        glue = {"io.github.dylanrusselldev.stepdefs", "io.github.dylanrusselldev.utilities"},   // Location of where the Step Definition and Utilities packages are
        plugin = {"pretty", "html:" + Constants.CUCUMBER_HTML_REPORT_PATH,                      // Location to generate HTML report
                "json:" + Constants.CUCUMBER_JSON_REPORT_PATH},                                 // Location to generate the JSON cucumber file
        monochrome = true)                                                                      // Console messages are more readable

public class Runner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
