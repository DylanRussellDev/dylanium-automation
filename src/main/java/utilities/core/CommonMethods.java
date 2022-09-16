/*
 * Filename: CommonMethods.java
 * Purpose: Commonly used methods for step definition files can be called
 *			from this file to keep the code readable.
 */

package utilities.core;

import static org.testng.Assert.fail;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.browsers.CaptureScreenshot;

public class CommonMethods {
	
	private static final ReadConfigFile readFile = new ReadConfigFile();


	/**
	 * Returns the browser information for the reports
	 */
	public static String browserInfo(Capabilities threadCap) {
		try {
			String str = threadCap.getBrowserName().equalsIgnoreCase("Msedge") ? "MS Edge" : threadCap.getBrowserName();
			String info = str + " " + threadCap.getBrowserVersion();
			return info.substring(0, 1).toUpperCase() + info.substring(1);
		} catch (Exception e) {
			System.out.println("Could not find browser name and version the tests were executed on");
			return null;
		} // end try catch
	} // end browserInfo

	/**
	 * Find an element and perform a click function on it
	 *
	 * @param driver 		WebDriver
	 * @param element 		The WebElement identifier
	 * @param str 			String of WebElement for assert message
	 */
	public static void click(WebDriver driver, By element, String str) {
		isElementPresent(driver, element, str);
		isElementClickable(driver, element, str);

		int i = 0;

		while (i < 5) {
			try {
				driver.findElement(element).click();
				break;
			} catch (ElementClickInterceptedException e) {
				fail("Could not click on element: " + str + " because it became detached from the DOM structure.\n");
			} catch (StaleElementReferenceException s) {
				fail("Could not click on element: " + str + " because another element was concealing it.\n");
			} catch (TimeoutException t) {
				fail("After clicking on: " + str + ", the page took too long to load. \n");
			} // end try-catch

			i++;
		} // end while


	} // end click
	
	/**
	 * Find an element and return its text
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
			fail("Could not get text from: " + str + "\n");
			return null;
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
			fail(str + " could not be hovered to.\n");
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
			fail(str + " could not be hovered over.\n");
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
			fail("Could not input text into element: " + str + "\n");
		} // end try-catch
	} // end input

	/**
	 * Checks to see if an element is able to be clicked
	 *
	 * @param driver  WebDriver
	 * @param element The WebElement identifier
	 * @param str     String of WebElement for assert message
	 */
	private static void isElementClickable(WebDriver driver, By element, String str) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(readFile.properties.getProperty("Timeout"))));
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			fail(str + " was not in a clickable state.\n");
		} // end try-catch
	} // end isElementPresent
	
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
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
		} catch (Exception e) {
			fail(str + " was not present in specified time.\n");
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
			fail("Could not navigate to: " + url + "\n");
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
			fail("Unable to pause execution for " + seconds + " seconds.\n");
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
		Hooks.scenario.get().attach(imgInBytes, "image/png", desc);
	} // end screenshot()

	/**
	 * Select an option from a dropdown list based off the order it appears
	 *
	 * @param driver 		WebDriver
	 * @param element 		The WebElement identifier
	 * @param index 		The number in the list where the desired option is
	 * @param str 			String of WebElement for assert message
	 */
	public static void selectDropdownOptionByIndex(WebDriver driver, By element, int index, String str) {
		isElementPresent(driver, element, str);
		Select select = new Select(driver.findElement(element));
		int i = 0;

		while (i < 5) {

			try {
				select.selectByIndex(index);
				break;
			} catch (ElementClickInterceptedException e) {
				fail(str + " could not be clicked because another element was concealing it.\n");
			} catch (NoSuchElementException n) {
				fail("There is not an option listed in the " + str + " at position " + index + "\n");
			} catch (StaleElementReferenceException s) {
				fail(str + " became detached from the DOM when trying to interact with it\n");
			} // end try catch
			i++;
		} // end while

	} // end selectDropdownOptionByIndex
	
} // end CommonMethods.java