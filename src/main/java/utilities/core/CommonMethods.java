/*
 * Filename: CommonMethods.java
 * Author: Dylan Russell
 * Purpose: Commonly used methods for step definition files can be called 
 *			from this file to keep the code readable.
 */

package utilities.core;

import static org.testng.Assert.fail;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.Command;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.browsers.CaptureScreenshot;

public class CommonMethods {
	
	private static final ReadConfigFile readFile = new ReadConfigFile();
	public static ArrayList<String> devtoolErrors = new ArrayList<>();

	/**
	 * Returns the browser information for the reports
	 */
	public static String browserInfo() {
		Capabilities cap = ((RemoteWebDriver) Hooks.driver).getCapabilities();
		String str = cap.getBrowserName() + " " + cap.getBrowserVersion();
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	} // end browserInfo
	/**
	 * Starts a DevTools listener. Anytime a failure is captured during execution, it is added
	 *  to an Array list to output on the report.
	 *
	 * @param driver 		WebDriver
	 */
	public static void captureDevTools(WebDriver driver) {
		try {
			DevTools dt = ((ChromeDriver) driver ).getDevTools();
			dt.createSessionIfThereIsNotOne();
			dt.send(new Command<>("Network.enable", ImmutableMap.of()));
			dt.addListener(Network.responseReceived(), receive -> {
				String strStatus = receive
						.getResponse()
						.getStatus()
						.toString();

					if (!strStatus.equals("200")) {
						devtoolErrors.add("DevTools error URL: " + receive
								.getResponse()
								.getUrl()
								.replace("https://", "") + "\n"
						+ "Status: "+ receive.getResponse().getStatus() + ", Error: " + receive.getResponse().getStatusText());
					}
			});
		} catch (Exception e) {
			System.out.println("Could not start DevTools listener\n");
		}
	}

	/**
	 * Find an element and perform a click function on it
	 *
	 * @param driver 		WebDriver
	 * @param element 		The WebElement identifier
	 * @param str 			String of WebElement for assert message
	 */
	public static void click(WebDriver driver, By element, String str) {
		isElementPresent(driver, element, str);
		try { 
			driver.findElement(element).click();
		} catch (Exception e) {
			fail("Could not click on element: " + str);
		} // end try-catch
	} // end click
	
	/**
	 * Find an element and get its text
	 * 
	 * @param driver 		WebDriver
	 * @param element 		The WebElement identifier
	 * @param str 			String of WebElement for assert message
	 */
	public static String getElementText(WebDriver driver, By element, String str) {
		isElementPresent(driver, element, str);
		try {
			String val = driver.findElement(element).getText();
			
			if (val.isEmpty()) { 
				val = driver.findElement(element).getAttribute("value");
			} // end if
			
			return val;
		} catch (Exception e) {
			return driver.findElement(element).getAttribute("value");
		} // end try-catch
	} // end getElementText()
	
	/**
	 * Find an element and hover on the element using the JavaScript function. 
	 * 
	 * @param driver 		WebDriver
	 * @param element 		The WebElement identifier
	 * @param str			String of WebElement for assert message
	 */
	public static void hoverJavascript(WebDriver driver, By element, String str) {
		isElementPresent(driver, element, str);
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].onmouseover()", driver.findElement(element));
		} catch (Exception e) {
			fail(str + " could not be hovered to.");
		} // end try-catch
	} // end hoverJavaScript
	
	/**
	 * Find an element and hover on the element using the Selenium function.
	 * 
	 * @param driver 		WebDriver
	 * @param element 		The WebElement identifier
	 * @param str 			String of WebElement for assert message
	 */
	public static void hoverSelenium(WebDriver driver, By element, String str) {
		isElementPresent(driver, element, str);
		try {
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(element)).perform();
		} catch (Exception e){
			fail(str + " could not be hovered over.");
		} // end try-catch
	} // end hoverSelenium
	
	/**
	 * Find an element and input text into it
	 * 
	 * @param driver 		WebDriver
	 * @param element 		The WebElement identifier
	 * @param input 		The string to input into the WebElement
	 * @param str 			String of WebElement for assert message
	 */
	public static void input(WebDriver driver, By element, String input, String str) {
		isElementPresent(driver, element, str);
		try {
			driver.findElement(element).sendKeys(input);
		} catch (Exception e) {
			fail("Could not input text into element: " + str);
		} // end try-catch
	} // end input
	
	/**
	 * Checks to see if an element is present on a web page within 60 seconds.
	 *
	 * @param driver  WebDriver
	 * @param element The WebElement identifier
	 * @param str     String of WebElement for assert message
	 */
	public static void isElementPresent(WebDriver driver, By element, String str) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(readFile.properties.getProperty("Timeout"))));
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		} catch (Exception e) {
			fail(str + " is not present in specified time");
		} // end try-catch
	} // end isElementPresent
	
	/**
	 * Navigates to a given URL
	 * 
	 * @param driver 		WebDriver
	 * @param url			URL as a String
	 */
	public static void navigate(WebDriver driver, String url)  {
		try {
			driver.get(readFile.properties.getProperty(url));
		} catch (Exception e) {
			fail("Could not navigate to: " + url);
		} // end try-catch
	} // end navigate()
	
	/**
	 * Returns the OS the tests are executing on to include in the report
	 * 
	 * @return String		OS as a string
	 */
	public static String osInfo() {
		String os = System.getProperty("os.name").toLowerCase();
		String strOS;
		if (os.contains("win")) {
			strOS = "Windows";
		} else if (os.contains("nux") || os.contains("nix")) {
			strOS = "Linux";
		} else if (os.contains("mac")) {
			strOS = "Mac";
		} else if (os.contains("sunos")) {
			strOS = "Solaris";
		} else {
			strOS = "Other";
		}
		return strOS;
	} // end osInfo()
	
	/**
	 * Pauses script execution for a desired amount of seconds.
	 * This method should only be used if Explicit waits are not working.
	 * 
	 * @param seconds		The amount of seconds to wait for. This accepts fractions of seconds as well.
	 */
	public static void pauseForSeconds(double seconds) {
		double milli = seconds * 1000;
		int intMilli = (int) milli;
		try {
			Thread.sleep(intMilli);
		} catch (Exception e) {
			fail("Unable to pause execution for " + seconds + " seconds.");
		} // end try-catch
	} // end pauseForSeconds()
	
	/**
	 * Takes a screenshot of the entire screen
	 * 
	 * @param driver 		WebDriver 
	 * @param desc			Text to display about screenshot in cucumber report
	 * @throws IOException  Error
	 */
	public static void screenshot(WebDriver driver, String desc) throws IOException {
		BufferedImage img = CaptureScreenshot.getScreenshot(driver);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ImageIO.write(img, "png", outStream);
		outStream.flush();
		byte[] imgInBytes = outStream.toByteArray();
		outStream.close();
		Hooks.scenario.attach(imgInBytes, "image/png", desc);
	} // end screenshot()
	
} // end CommonMethods.java