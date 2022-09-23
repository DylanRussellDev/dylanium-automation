/*
 * Filename: GenerateReport.java
 * Purpose: Generates the Masterthought test execution report.
 * 			The file to open in the target directory is: overview-features.html file
 */

package utilities.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class GenerateReport {

	public static void GenerateTestNGReport() {
		
		try {

			// The folder to output the reports to. If the folder does not exist, it will be created.
			File outDirectory = new File("target/~REPORT");
			List<String> list = new ArrayList<>();
			list.add("target/cucumber-reports/cucumber.json");

			// Formatting
			Configuration config = new Configuration(outDirectory, "Tests");
			config.addClassifications("OS", CommonMethods.osInfo());
			config.addClassifications("Browser", CommonMethods.browserInfo(Hooks.cap));

			// Generate the report
			ReportBuilder repBuild = new ReportBuilder(list, config);
			repBuild.generateReports();

		} catch (Exception e) {

			System.out.println("There was a problem generating the report.");
			System.out.println("Error Message: " + e.getMessage());

		} // end try/catch block
		
	} // end GenerateTestNGReport()

} // end class GenerateReport.java