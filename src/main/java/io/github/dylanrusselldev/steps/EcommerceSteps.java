package io.github.dylanrusselldev.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.webelements.eCommerceObjects;
import org.openqa.selenium.WebDriver;

public class EcommerceSteps {

    private final WebDriver driver;

    public EcommerceSteps() {
        this.driver = Hooks.getDriver();
    }

    @Given("the user navigates to the ecommerce site")
    public void the_user_navigates_to_the_ecommerce_site() {
        CommonMethods.navigate(driver, "ecommerceURL");
    }

    @Given("the user goes to the Latops and Notebooks section of the site")
    public void the_user_goes_to_the_latops_and_notebooks_section_of_the_site() {
        CommonMethods.hoverSelenium(driver, eCommerceObjects.ddlLaptopsNotebooks, "Laptops & Notebooks dropdown list");
        CommonMethods.click(driver, eCommerceObjects.lnkShowAllLaptops, "Show All Laptops & Notebooks link");
    }

    @When("the user selects the Compare button for the {string}")
    public void the_user_selects_the_compare_button_for_the(String product) {
        switch (product) {
            case "MacBook Air":
                CommonMethods.click(driver, eCommerceObjects.btnCompareMacBookAir, "MacBook Air Compare button");
                CommonMethods.isElementPresent(driver, eCommerceObjects.msgSuccessProductComparison, "Success Message");
                CommonMethods.fullScreenshot(driver);
                break;

            case "MacBook Pro":
                CommonMethods.click(driver, eCommerceObjects.btnCompareMacBookPro, "MacBook Pro Compare button");
                CommonMethods.isElementPresent(driver, eCommerceObjects.msgSuccessProductComparison, "Success Message");
                CommonMethods.fullScreenshot(driver);
                break;
        }
    }

    @When("clicks the Product Compare link")
    public void clicks_the_product_compare_link() {
        CommonMethods.click(driver, eCommerceObjects.lnkProductCompare, "Product Compare button");
    }

    @Then("the two Macbooks will be compared side by side")
    public void the_two_macbooks_will_be_compared_side_by_side() {
        CommonMethods.isElementPresent(driver, eCommerceObjects.lblMacBookAir, "MacBook Air link text");
        CommonMethods.isElementPresent(driver, eCommerceObjects.lblMacBookPro, "MacBook Pro link text");
        CommonMethods.fullScreenshot(driver);
    }

}