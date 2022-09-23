/*
 * Filename: Runner.java
 * Purpose: The cucumber options for test execution.
 * 			If test execution is not working, check the correct
 * 			package names and folder locations are used.
 */

package runnerClasses;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = {"src/test/java/features"},								// Location of where the Feature File package is
				glue = {"stepDefinitions", "utilities"},							// Location of where the Step Definition and Utilities packages are
				plugin = {"pretty", "html:target/cucumber-reports/report.html",		// Location to generate HTML report
									"json:target/cucumber-reports/cucumber.json"},	// Location to generate the JSON cucumber file
				monochrome = true)													// Console messages are more readable
public class Runner extends AbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.scenarios();
	} // end scenarios

} // end Runner.java
