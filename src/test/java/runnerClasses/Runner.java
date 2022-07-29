/*
 * Filename: Runner.java
 * Author: Dylan Russell
 * Purpose: The cucumber options for test execution are found here. 
 * 			If test execution is not working, double check the correct
 * 			package names and folder locations are used.
 */

package runnerClasses;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(features = {"src/test/java/features"},								// Location of the feature file package
										glue = {"stepDefinitions", "utilities"},							// Package names where the step definition files and utilities are
										plugin = {"pretty", "html:target/cucumber-reports/report.html",		// Location of the reports after test execution has finished
												"json:target/cucumber-reports/cucumber.json"},				// Location of the JSON cucumber file
										monochrome = true)													// Make console easier to read

public class Runner extends AbstractTestNGCucumberTests {}