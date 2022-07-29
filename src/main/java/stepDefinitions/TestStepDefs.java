/*
 * Filename: TestStepDefs.java
 * Author: Dylan Russell
 * Purpose: File to hold code for the feature file steps
 */

package stepDefinitions;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.core.CommonMethods;
import utilities.core.Hooks;
import pagesElements.TestObjects;

public class TestStepDefs {

	private WebDriver driver;
//	private Map<String, String> collection = new HashMap<String, String>();	
	
	// Instantiate the WebDriver
	public TestStepDefs() {
		this.driver = Hooks.getDriver();
	} // end constructor
	
	@Given("^I navigate to the calculator website$")
	public void i_navigate_to_the_calculator_website() {
		CommonMethods.navigate(driver, "calculatorURL");
	}

	@When("I click {string} plus {string}")
	public void i_click_plus(String num1, String num2) {
		CommonMethods.click(driver, TestObjects.btn2,  "2 Button");
	    CommonMethods.click(driver, TestObjects.btnPlus, "Plus Button");
		CommonMethods.click(driver, TestObjects.btn2,  "2 Button");
	    CommonMethods.click(driver, TestObjects.btnEquals, "Equals Button");
	}

	@Then("verify the output is {string}")
	public void verify_the_output_is(String answer) throws Throwable {
		String result = CommonMethods.getElementText(driver, TestObjects.txtOutput, "Result").replaceAll("\\s", "");
		System.out.println("result: " + result);
		System.out.println("answer: " + answer);
		assertEquals(answer, result, "Output: " + answer + " is not correct");
		CommonMethods.screenshot(driver, "Output Screenshot");
	}
	
	@Given("I navigate to Google")
	public void i_navigate_to_google() {
		CommonMethods.navigate(driver, "testURL");
	}

	@When("I get to Google")
	public void i_get_to_google() {
	    
	}

	@Then("I take a screenshot")
	public void i_take_a_screenshot() throws IOException {
		CommonMethods.screenshot(driver, "Output Screenshot");
	}
	
}
