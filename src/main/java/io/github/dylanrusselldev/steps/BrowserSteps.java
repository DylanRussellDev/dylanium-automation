package io.github.dylanrusselldev.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.webelements.EdgeBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;

public class BrowserSteps {

	private final WebDriver driver;
	private static final LoggerClass LOGGER = new LoggerClass(BrowserSteps.class);
	private static final String CHECKING_FOR_UPDATES = "Checking for updates";
	private static final String IS_UP_TO_DATE = "is up to date";
	private static final String UPDATING = "Updating";
	private static final String CHROME_RELAUNCH = "Nearly up to date";
	private static final String EDGE_RELAUNCH = "restart Microsoft Edge";

	public BrowserSteps() {
		this.driver = Hooks.getDriver();
	}
	
	private String getChromeUpdateText() {
		WebElement root1 = driver.findElement(By.tagName("settings-ui"));
		SearchContext shadowroot1 = root1.getShadowRoot();

		WebElement root2 = shadowroot1.findElement(By.cssSelector("settings-main#main"));
		SearchContext shadowroot2 = root2.getShadowRoot();

		WebElement root3 = shadowroot2.findElement(By.cssSelector("settings-about-page.cr-centered-card-container"));
		SearchContext shadowroot3 = root3.getShadowRoot();

		WebElement lblUpdateText = shadowroot3.findElement(By.cssSelector("div#updateStatusMessage div"));

		return lblUpdateText.getText();
	}
	
	@Given("I am on the About Google page")
	public void i_am_on_the_about_google_page() {
		CommonMethods.navigate(driver, "aboutChromeURL");
	}

	@Then("check if Google Chrome has an available update")
	public void check_if_google_chrome_has_an_available_update() {

	    if (getChromeUpdateText().contains(CHECKING_FOR_UPDATES) || getChromeUpdateText().contains(UPDATING)) {
	    	
	    	for (int i = 0; i < 12; i++) {

	    		String txt = getChromeUpdateText();
	    		LOGGER.info("Chrome Update Status: " + txt);
	    		
	    		if (txt.contains(CHROME_RELAUNCH) || txt.contains(IS_UP_TO_DATE)) {
	    			break;
	    		} else {
	    			CommonMethods.pauseForSeconds(5);
	    		}
	    		
	    	}
	    	
	    } else {
			LOGGER.info("Chrome is up to date");
	    }
	}
	
	@Then("close Chrome {string} if a new update needs applied")
	public void relaunch_chrome_if_a_new_update_needs_applied(String version) throws IOException {

		if (getChromeUpdateText().contains(CHROME_RELAUNCH)) {

	    	switch (version) {

                case "Stable":
                    Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
                    break;

                case "Beta":
                    Runtime.getRuntime().exec("taskkill /F /IM chromedriver-beta.exe");
                    break;

                case "Dev":
                    Runtime.getRuntime().exec("taskkill /F /IM chromedriver-dev.exe");
                    break;

	    	}

	    }

	}

	@Given("I am on the About Microsoft Edge page")
	public void i_am_on_the_about_microsoft_edge_page() {
		CommonMethods.navigate(driver, "aboutEdgeURL");
	}

	@Then("check if Edge has an available update")
	public void check_if_edge_has_an_available_update() {
		String upTxt = CommonMethods.getElementText(driver, EdgeBrowser.lblUpdateStatus, "Update status text");

	    if (upTxt.contains(CHECKING_FOR_UPDATES) || upTxt.contains(UPDATING)) {
	    	
	    	for (int i = 0; i < 12; i++) {

	    		String txt = CommonMethods.getElementText(driver, EdgeBrowser.lblUpdateStatus, "Update status text");
				LOGGER.info("Edge Update Status: " + txt);
	    		
	    		if (txt.contains(EDGE_RELAUNCH) || txt.contains(IS_UP_TO_DATE)) {
	    			break;
	    		} else {
	    			CommonMethods.pauseForSeconds(5);
	    		}

	    	}
	    	
	    } else {
			LOGGER.info("Edge is up to date");
	    }

	}

	@Then("relaunch Edge if a new update needs applied")
	public void relaunch_edge_if_a_new_update_needs_applied() {
		String strUpdateStatus = CommonMethods.getElementText(driver, EdgeBrowser.lblUpdateStatus, "Update status text");

		if (strUpdateStatus.contains(EDGE_RELAUNCH)) {
	    	driver.quit();
	    }
	}
	
}
