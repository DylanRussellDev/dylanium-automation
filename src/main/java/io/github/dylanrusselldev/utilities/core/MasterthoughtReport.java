/*
 * Filename: MasterthoughtReport.java
 * Author: Dylan Russell
 * Purpose: Generates the Masterthought test execution report.
 * 			The file to open in the target directory is: overview-features.html file
 */

package io.github.dylanrusselldev.utilities.core;

import io.github.dylanrusselldev.utilities.runtime.RuntimeInfo;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.presentation.PresentationMode;
import org.slf4j.event.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MasterthoughtReport {

    private static final LoggerClass LOGGER = new LoggerClass(MasterthoughtReport.class);

    /**
     * Generate the Masterthought report
     */
    public static void generateTestReport() {

        LOGGER.log(Level.INFO, "*** Now generating the Masterthought report ***");

        try {

            // Folder where the reports are generated
            File outDirectory = new File(Constants.MASTERTHOUGHT_REPORT_PATH);

            // Set the cucumber JSON file
            List<String> list = new ArrayList<>();
            list.add(Constants.CUCUMBER_JSON_REPORT_PATH);

            // Configuration object for adding custom formatting
            Configuration config = new Configuration(outDirectory, "Tests");

            // Add OS and Browser info
            config.addClassifications("OS", RuntimeInfo.getOSInfo());
            config.addClassifications("Browser", RuntimeInfo.getBrowserVersion(Hooks.cap));

            // Automatically expand all steps in the report
            config.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);

            // Generate the report
            ReportBuilder repBuild = new ReportBuilder(list, config);
            repBuild.generateReports();

        } catch (Exception e) {

            LOGGER.logAndFail("Error encountered when generating the Masterthought report.", e);

        } // end try-catch

    } // end GenerateTestReport()

} // end class MasterthoughtReport.java