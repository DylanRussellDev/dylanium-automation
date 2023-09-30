package runners;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.github.dylanrusselldev.utilities.core.Constants;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/resources/features",
        glue = {"io.github.dylanrusselldev.steps", "io.github.dylanrusselldev.utilities"},
        plugin = {"pretty",
                "html:" + Constants.CUCUMBER_HTML_REPORT_PATH,
                "json:" + Constants.CUCUMBER_JSON_REPORT_PATH},
        monochrome = true)

public class Runner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
