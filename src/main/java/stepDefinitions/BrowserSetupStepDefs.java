package stepDefinitions;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utilities.core.CommonMethods;
import utilities.core.Constants;
import utilities.core.Hooks;
import pagesElements.EdgeBrowser;

public class BrowserSetupStepDefs {

	private final WebDriver driver;
//	private Map<String, String> collection = new HashMap<String, String>();
	public BrowserSetupStepDefs() {
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

	    if (getChromeUpdateText().contains(Constants.CHECKINGFORUPDATES)
				|| getChromeUpdateText().contains(Constants.UPDATING)) {
	    	
	    	for (int i = 0; i < 12; i++) {
	    		String txt = getChromeUpdateText();
	    		System.out.println("Chrome Update Status: " + txt);
	    		
	    		if (txt.contains(Constants.CHROMERELAUNCH) || txt.contains(Constants.UPTODATE)) {
	    			break;
	    		} else {
	    			CommonMethods.pauseForSeconds(5);
	    		} // end inner if-else
	    		
	    	} // end for
	    	
	    } else {
	    	System.out.println("Chrome is up to date");
	    } // end outer if-else
	}
	
	@Then("close Chrome {string} if a new update needs applied")
	public void relaunch_chrome_if_a_new_update_needs_applied(String version) throws IOException {
		if (getChromeUpdateText().contains(Constants.CHROMERELAUNCH)) {
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
	    	} // end switch	
	    } // end if
	}

	@Given("I am on the About Microsoft Edge page")
	public void i_am_on_the_about_microsoft_edge_page() {
		CommonMethods.navigate(driver, "aboutEdgeURL");
	}

	@Then("check if Edge has an available update")
	public void check_if_edge_has_an_available_update() {
		String upTxt = CommonMethods.getElementText(driver, EdgeBrowser.lblUpdateStatus, "Update status text");
	    if (upTxt.contains(Constants.CHECKINGFORUPDATES) || upTxt.contains(Constants.UPDATING)) {
	    	
	    	for (int i = 0; i < 12; i++) {
	    		String txt = CommonMethods.getElementText(driver, EdgeBrowser.lblUpdateStatus, "Update status text");
	    		System.out.println("Edge Update Status: " + txt);
	    		
	    		if (txt.contains(Constants.EDGERELAUNCH) || txt.contains(Constants.UPTODATE)) {
	    			break;
	    		} else {
	    			CommonMethods.pauseForSeconds(5);
	    		} // end inner if-else
	    		
	    	} // end for
	    	
	    } else {
	    	System.out.println("Edge is up to date");
	    } // end outer if-else
	}

	@Then("relaunch Edge if a new update needs applied")
	public void relaunch_edge_if_a_new_update_needs_applied() {
		String strUpdateStatus = CommonMethods.getElementText(driver, EdgeBrowser.lblUpdateStatus, "Update status text");

		if (strUpdateStatus.contains(Constants.EDGERELAUNCH)) {
	    	driver.quit();
	    } // end if
	}
	
} // end BrowserSetupStepDefs.java
