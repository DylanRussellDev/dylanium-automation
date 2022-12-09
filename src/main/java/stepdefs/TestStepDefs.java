package stepdefs;

import static org.testng.Assert.assertEquals;

import elements.DemoSiteObjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.core.CommonMethods;
import utilities.core.Hooks;
import elements.CalculatorObjects;
import utilities.core.ReadConfigFile;
import utilities.helpers.ScreenRecorderUtil;

import java.io.IOException;

public class TestStepDefs {

    private final WebDriver driver;
    private static final ReadConfigFile propFile = new ReadConfigFile();

    public TestStepDefs() {
        this.driver = Hooks.getDriver();
    } // end constructor

    @Given("the user navigates to the calculator website")
    public void the_user_navigates_to_the_calculator_website() throws Exception {
        CommonMethods.navigate(driver, "calculatorURL");
        ScreenRecorderUtil.startRecord("navigate");
    }

    @When("the user clicks two plus two")
    public void the_user_clicks_two_plus_two() {
        CommonMethods.click(driver, CalculatorObjects.btn2, "2 Button");
        CommonMethods.click(driver, CalculatorObjects.btnPlus, "Plus Button");
        CommonMethods.click(driver, CalculatorObjects.btn2, "2 Button");
        CommonMethods.click(driver, CalculatorObjects.btnEquals, "Equals Button");
    }

    @Then("verify the output is {string}")
    public void verify_the_output_is(String answer) throws Throwable {
        String result = CommonMethods.getElementText(driver, CalculatorObjects.txtOutput, "Result").replaceAll("\\s", "");
        assertEquals(answer, result, "Output: " + answer + " is not correct");
        CommonMethods.screenshot(driver);
        ScreenRecorderUtil.stopRecord();
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
    public void a_success_message_will_be_displayed() throws IOException {
        CommonMethods.isElementPresent(driver, DemoSiteObjects.msgSuccessLogin, "Login Successful message");
        CommonMethods.partialScreenshot(driver);
    }

} // end TestStepDefs.java
