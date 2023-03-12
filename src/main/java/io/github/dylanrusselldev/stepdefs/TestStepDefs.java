package io.github.dylanrusselldev.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.dylanrusselldev.elements.CalculatorObjects;
import io.github.dylanrusselldev.elements.DemoSiteObjects;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Hooks;
import io.github.dylanrusselldev.utilities.core.ReadConfigFile;
import io.github.dylanrusselldev.utilities.helpers.ScreenRecorderUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertEquals;

public class TestStepDefs {

    private final WebDriver driver;
    private static final ReadConfigFile propFile = new ReadConfigFile();

    public TestStepDefs() {
        this.driver = Hooks.getDriver();
    } // end constructor

    @Given("the user navigates to the calculator website")
    public void the_user_navigates_to_the_calculator_website() {
        CommonMethods.navigate(driver, "calculatorURL");
    }

    @When("the user clicks two plus two")
    public void the_user_clicks_two_plus_two() {
        CommonMethods.click(driver, CalculatorObjects.btn2, "2 Button");
        CommonMethods.click(driver, CalculatorObjects.btnPlus, "Plus Button");
        CommonMethods.click(driver, CalculatorObjects.btn2, "2 Button");
        CommonMethods.click(driver, CalculatorObjects.btnEquals, "Equals Button");
    }

    @Then("verify the output is {string}")
    public void verify_the_output_is(String answer) {
        String result = CommonMethods.getElementText(driver, CalculatorObjects.txtOutput, "Result").replaceAll("\\s", "");
        assertEquals(answer, result, "Output: " + answer + " is not correct");
        CommonMethods.screenshot(driver);
    }

    @Given("the screen recorder is started")
    public void the_screen_recorder_is_started() throws Exception {
        ScreenRecorderUtil.startRecord("navigate");
    }

    // SELENIUM DEMO SITE STEPS

    @Given("the user navigates to the Selenium Demo website")
    public void the_user_navigates_to_the_selenium_demo_website() {
        CommonMethods.navigate(driver, "demoSiteURL");
    }

    @When("the user selects the {string} as an Available Example")
    public void the_user_selects_the_as_an_available_example(String page) {
        CommonMethods.click(driver, By.xpath(DemoSiteObjects.lnkAvailableExamples.replace("LINK_TEXT", page)),
                page + " Available Example option");
    }

    @Then("the user is able to input {string} into the text area")
    public void the_user_is_able_to_input_into_the_text_area(String text) {
        CommonMethods.switchiFrame(driver, DemoSiteObjects.iframeWYSIWYG, "WYSIWYG iFrame");
        CommonMethods.input(driver, DemoSiteObjects.txtTinyMCE, text, "Text area box");
    }

    @Then("the screen recorder is stopped")
    public void the_screen_recorder_is_stopped() throws Exception {
        ScreenRecorderUtil.stopRecord();
    }

    @When("the user enters the username")
    public void the_user_enters_the_username() {
        CommonMethods.input(driver, DemoSiteObjects.txtUsername, propFile.properties.getProperty("demoUser"), "Username text box");
    }

    @When("the user enters the decrypted password")
    public void the_user_enters_the_decrypted_password() {
        CommonMethods.input(driver, DemoSiteObjects.txtPassword, CommonMethods.decrypt("demoPass"), "Password text box");
    }

    @When("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        CommonMethods.click(driver, DemoSiteObjects.btnLogin, "Login button");
    }

    @Then("a success message will be displayed")
    public void a_success_message_will_be_displayed() {
        CommonMethods.isElementPresent(driver, DemoSiteObjects.msgSuccessLogin, "Login Successful message");
        CommonMethods.partialScreenshot(driver);
    }

    @When("the user clicks the {string} code")
    public void the_user_clicks_the_code(String code) {
        CommonMethods.click(driver, By.xpath(DemoSiteObjects.lnkStatusCodes.replace("STATUS_CODE", code)),
                "Status Code " + code + " option");
    }

    @Then("the error information is captured and will be available in the report")
    public void the_error_information_is_captured_and_will_be_available_in_the_report() {
        CommonMethods.partialScreenshot(driver);
    }

} // end TestStepDefs.java
