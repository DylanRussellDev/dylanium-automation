package io.github.dylanrusselldev.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.dylanrusselldev.utilities.browser.DevToolsListener;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.core.Constants;
import io.github.dylanrusselldev.utilities.filereaders.PDFValidation;
import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.utilities.screenrecorder.ScreenRecorderUtil;
import io.github.dylanrusselldev.webelements.CalculatorObjects;
import io.github.dylanrusselldev.webelements.DemoSiteObjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class DemoSteps {

    private final WebDriver driver;
    private static final ReadConfigFile readConfigFile = new ReadConfigFile(Constants.PROP_FILEPATH + "\\automation.properties");
    private static final LoggerClass LOGGER = new LoggerClass(DemoSteps.class);

    public DemoSteps() {
        this.driver = Hooks.getDriver();
    }

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

    @When("the correct result is {string}")
    public void the_correct_result_is(String answer) {
        String result = CommonMethods.getElementText(driver, CalculatorObjects.txtOutput, "Result").replaceAll("\\s", "");
        LOGGER.info("Calculated Result: " + answer);
        assertEquals(answer, result, "Output: " + answer + " is not correct");
        CommonMethods.fullScreenshot(driver);
    }

    @Given("the screen recorder is started")
    public void the_screen_recorder_is_started() throws Exception {
        ScreenRecorderUtil.startRecord();
    }

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
        CommonMethods.enterText(driver, DemoSiteObjects.txtTinyMCE, text, "Text area box");
    }

    @Then("the screen recorder is stopped")
    public void the_screen_recorder_is_stopped() throws Exception {
        ScreenRecorderUtil.stopRecord();
    }

    @When("the user enters the username")
    public void the_user_enters_the_username() {
        CommonMethods.enterText(driver, DemoSiteObjects.txtUsername, readConfigFile.properties.getProperty("demoUser"), "Username text box");
    }

    @When("the user enters the decrypted password")
    public void the_user_enters_the_decrypted_password() {
        CommonMethods.enterText(driver, DemoSiteObjects.txtPassword, CommonMethods.decrypt("demoPass"), "Password text box");
    }

    @When("the user clicks the login button")
    public void the_user_clicks_the_login_button() {
        CommonMethods.click(driver, DemoSiteObjects.btnLogin, "Login button");
    }

    @Then("a success message is displayed")
    public void a_success_message_is_displayed() {
        CommonMethods.isElementPresent(driver, DemoSiteObjects.msgSuccessLogin, "Login Successful message");
        CommonMethods.partialScreenshot(driver);
    }

    @When("the user clicks the {string} code")
    public void the_user_clicks_the_code(String code) {
        DevToolsListener devToolsListener = new DevToolsListener(driver);
        devToolsListener.startDevToolsListener();

        CommonMethods.click(driver, By.xpath(DemoSiteObjects.lnkStatusCodes.replace("STATUS_CODE", code)),
                "Status Code " + code + " option");
    }

    @Then("the DevTools error information is logged in the report")
    public void the_DevTools_error_information_is_logged_in_the_report() {
        CommonMethods.isElementPresent(driver, DemoSiteObjects.lbl500Text, "500 status code text");
        CommonMethods.partialScreenshot(driver);
        DevToolsListener.logDevToolErrors();
    }

    @When("the user attempts to interact with a label")
    public void the_user_attempts_to_interact_with_a_label() {
        CommonMethods.isElementPresent(driver, DemoSiteObjects.hdInputs, "Inputs header text");
    }

    @Then("the scenario will fail with a user friendly exception message displayed on the report")
    public void the_scenario_will_fail_with_a_user_friendly_exception_message_displayed_on_the_report() {
        CommonMethods.enterText(driver, DemoSiteObjects.hdInputs, "text comment", "Inputs header text");
    }

    @Then("the user's actions are logged when different buttons are clicked")
    public void the_user_s_actions_are_logged_when_different_buttons_are_clicked() {
        CommonMethods.click(driver, DemoSiteObjects.hdAddRemoveElements, "Add/Remove Elements heading");
        CommonMethods.click(driver, DemoSiteObjects.btnAddElement, "Add Element button");
        LOGGER.logCucumberReport("User clicked on the Add Element button");
        CommonMethods.click(driver, DemoSiteObjects.btnDelete, "Delete button");
        LOGGER.logCucumberReport("User clicked on the Delete button");
    }

    @Then("take a screenshot of the page with the username and password fields blurred out")
    public void take_a_screenshot_of_the_page_with_the_username_and_password_fields_blurred_out() {
        CommonMethods.enterText(driver, DemoSiteObjects.txtUsername, readConfigFile.properties.getProperty("demoUser"), "Username text box");
        CommonMethods.blurElement(driver, DemoSiteObjects.txtUsername, "Username text box");

        CommonMethods.enterText(driver, DemoSiteObjects.txtPassword, CommonMethods.decrypt("demoPass"), "Password text box");
        CommonMethods.blurElement(driver, DemoSiteObjects.txtPassword, "Username text box");

        CommonMethods.pauseForSeconds(3);
        CommonMethods.partialScreenshot(driver);
    }

    @When("the user downloads the sample pdf")
    public void the_user_downloads_the_sample_pdf() {
        CommonMethods.click(driver, DemoSiteObjects.lnkSamplePDF, "Sample PDF link");
    }

    @Then("the downloaded PDF contains the text {string}")
    public void the_downloaded_pdf_contains_the_text(String text) throws IOException {
        PDFValidation.verifyDownloadedPDFText(driver, text, "sample.pdf");
    }

}
