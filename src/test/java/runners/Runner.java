package runners;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.github.dylanrusselldev.utilities.core.Constants;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/resources/features",                                           // Location of where the Feature File package is
        glue = {"io.github.dylanrusselldev.steps", "io.github.dylanrusselldev.utilities"},   // Location of where the Step Definition and Utilities packages are
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
