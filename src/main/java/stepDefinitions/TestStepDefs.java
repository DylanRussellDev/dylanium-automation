package stepDefinitions;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.core.CommonMethods;
import utilities.core.Hooks;
import pageElements.TestObjects;

public class TestStepDefs {

	private final WebDriver driver;

	// Instantiate the WebDriver
	public TestStepDefs() {
		this.driver = Hooks.getDriver();
	} // end constructor
	
	@Given("^I navigate to the calculator website$")
	public void i_navigate_to_the_calculator_website() {
		CommonMethods.navigate(driver, "calculatorURL");
	}

	@When("I click two plus two")
	public void i_click_two_plus_two() {
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
	    CommonMethods.isElementPresent(driver, TestObjects.imgGoogleLogo, "Google Logo image");
	}

	@Then("I take a screenshot")
	public void i_take_a_screenshot() throws IOException {
		CommonMethods.screenshot(driver, "Output Screenshot");
	}
	
} // end TestStepDefs.java
