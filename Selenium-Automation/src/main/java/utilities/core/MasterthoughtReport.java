/*
 * Filename: MasterthoughtReport.java
 * Purpose: Generates the Masterthought test execution report.
 * 			The file to open in the target directory is: overview-features.html file
 */

package utilities.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.presentation.PresentationMode;

public class MasterthoughtReport {

	public static void GenerateTestReport() {
		
		try {
			// Folder where the reports are generated
			File outDirectory = new File("target/~Masterthought-Report");
			List<String> list = new ArrayList<>();
			list.add("target/cucumber-reports/cucumber.json");

			// Configuration object for adding custom formatting
			Configuration config = new Configuration(outDirectory, "Tests");

			// Add OS and Browser info
			config.addClassifications("OS", CommonMethods.osInfo());
			config.addClassifications("Browser", CommonMethods.browserInfo(Hooks.cap));

			// Automatically expand all steps in the report
			config.addPresentationModes(PresentationMode.EXPAND_ALL_STEPS);

			// Generate the report
			ReportBuilder repBuild = new ReportBuilder(list, config);
			repBuild.generateReports();

		} catch (Exception e) {
			System.out.println("There was a problem generating the Masterthought report.\n"
			+ "Error Message: " + e.getMessage());
		} // end try/catch block
		
	} // end GenerateTestReport()

} // end class MasterthoughtReport.java