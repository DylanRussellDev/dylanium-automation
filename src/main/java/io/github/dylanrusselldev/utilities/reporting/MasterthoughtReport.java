package io.github.dylanrusselldev.utilities.reporting;

import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.presentation.PresentationMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * Filename: MasterthoughtReport.java
 * Author: Dylan Russell
 * Purpose: Generates the Masterthought test execution report.
 */
public class MasterthoughtReport {

    private static final LoggerClass LOGGER = new LoggerClass(MasterthoughtReport.class);

    /**
     * Generate the Masterthought report after execution has finished.
     */
    public static void generateTestReport() {

        LOGGER.info("*** Now generating the Masterthought report ***");

        try {
            File outDirectory = new File(Constants.MASTERTHOUGHT_REPORT_PATH);
            List<String> list = new ArrayList<>();
            list.add(Constants.CUCUMBER_JSON_REPORT_PATH);

            Configuration configuration = new Configuration(outDirectory, "Tests");
            configuration.addClassifications("OS", RuntimeInfo.getOSInfo());
            configuration.addClassifications("Browser", RuntimeInfo.getBrowserVersion(RuntimeInfo.capabilities));
            configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);

            ReportBuilder reportBuilder = new ReportBuilder(list, configuration);
            reportBuilder.generateReports();
        } catch (Exception e) {
            LOGGER.fail("Error encountered when generating the Masterthought report.", e);
        }

    }

}
