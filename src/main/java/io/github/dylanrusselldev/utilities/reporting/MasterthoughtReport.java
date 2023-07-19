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
 * 			The file to open in the target directory is: overview-features.html file
 */
public class MasterthoughtReport {

    private static final LoggerClass LOGGER = new LoggerClass(MasterthoughtReport.class);

    /**
     * Generate the Masterthought report after execution has finished.
     */
    public static void generateTestReport() {

        LOGGER.info("*** Now generating the Masterthought report ***");

        try {

            // Folder where the reports are generated
            File outDirectory = new File(Constants.MASTERTHOUGHT_REPORT_PATH);

            // Set the cucumber JSON file
            List<String> list = new ArrayList<>();
            list.add(Constants.CUCUMBER_JSON_REPORT_PATH);

            // Configuration object for adding custom formatting
            Configuration configuration = new Configuration(outDirectory, "Tests");

            // Add OS and Browser info
            configuration.addClassifications("OS", RuntimeInfo.getOSInfo());
            configuration.addClassifications("Browser", RuntimeInfo.getBrowserVersion(RuntimeInfo.capabilities));

            // Automatically expand all steps in the report
            configuration.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);

            // Generate the report
            ReportBuilder reportBuilder = new ReportBuilder(list, configuration);
            reportBuilder.generateReports();

        } catch (Exception e) {

            LOGGER.logAndFail("Error encountered when generating the Masterthought report.", e);

        } // end try-catch

    }

}
