/*
 * Filename: CommonMethods.java
 * Purpose: Commonly used methods for step definition files can be called
 *			from this file to keep the code readable.
 */

package utilities.core;

import static org.testng.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.browsers.CaptureScreenshot;

public class CommonMethods {

    private static final ReadConfigFile propFile = new ReadConfigFile();

    /**
     * Returns the browser information for the reports
     *
     * @param threadCap The Capabilities information passed in from Hooks
     */
    public static String browserInfo(Capabilities threadCap) {

        try {

            String str = threadCap.getBrowserName().equalsIgnoreCase("Msedge") ? "MS Edge" : threadCap.getBrowserName();
            String info = str + " " + threadCap.getBrowserVersion();
            return info.substring(0, 1).toUpperCase() + info.substring(1);

        } catch (Exception e) {

            System.out.println("Could not find browser name and version the tests were executed on \n");
            return null;

        } // end try catch

    } // end browserInfo()

    /**
     * Find an element and perform a click function on it
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
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

    } // end click()

    /**
     * Decrypts an encrypted string. Pass the property name of the string to decrypt
     *
     * @param property The property name of the string to decrypt
     */
    public static String decrypt(String property) {

        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();

        decryptor.setAlgorithm(propFile.properties.getProperty("algorithm"));
        decryptor.setPassword(propFile.properties.getProperty("secretPass"));
        decryptor.setIvGenerator(new RandomIvGenerator());
        decryptor.setKeyObtentionIterations(Integer.parseInt(propFile.properties.getProperty("keyIterations")));

        return decryptor.decrypt(propFile.properties.getProperty(property));

    } // end decrypt()

    /**
     * Find an element and return the text in it. If the .getText() attempt fails,
     * the script will attempt to find the "value" attribute
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
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
     * Returns the most recent file in a folder location with a
     * given file extension
     *
     * @param folderPath Folder location
     * @param ext        The filename extension to look for
     */
    public static File getNewestFile(String folderPath, String ext) {

        File newestFile = null;
        File dir = new File(folderPath);
        FileFilter fileFilter = new WildcardFileFilter("*." + ext);
        File[] files = dir.listFiles(fileFilter);

        if (files.length > 0) {

            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            newestFile = files[0];

        } else {

            fail("There were no files found in the folder path: " + folderPath);

        } // end if-else

        return newestFile;
    } // end getNewestFile()

    private static String ogStyle;

    /**
     * Highlights an element in yellow with a red outline around it.
     * This makes monitoring and debugging multiple validations much easier.
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    public static void highlightElement(WebDriver driver, By element, String str) {

        isElementPresent(driver, element, str);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement ele = driver.findElement(element);
        ogStyle = ele.getAttribute("style");

        try {

            js.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;'):", ele);

        } catch (Exception e) {

            fail("Unable to highlight element: " + str + "\n");

        } // end try-catch

    } // end highlightElement()

    /**
     * Find an element and hover on the element using the JavaScript function.
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    public static void hoverJavascript(WebDriver driver, By element, String str) {

        isElementPresent(driver, element, str);

        try {

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].onmouseover()", driver.findElement(element));

        } catch (Exception e) {

            fail(str + " could not be hovered to.\n");

        } // end try-catch

    } // end hoverJavaScript()

    /**
     * Find an element and hover on the element using the Selenium function.
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    public static void hoverSelenium(WebDriver driver, By element, String str) {

        isElementPresent(driver, element, str);

        try {
            Actions action = new Actions(driver);
            action.moveToElement(driver.findElement(element)).perform();

        } catch (Exception e) {

            fail(str + " could not be hovered over.\n");

        } // end try-catch

    } // end hoverSelenium()

    /**
     * Find an element and input text into it
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param input   The string to input into the WebElement
     * @param str     String of WebElement for assert message
     */
    public static void input(WebDriver driver, By element, String input, String str) {

        isElementPresent(driver, element, str);

        try {

            driver.findElement(element).sendKeys(input);

        } catch (Exception e) {

            fail("Could not input text: " + input + " into element: " + str + "\n");

        } // end try-catch

    } // end input()

    /**
     * Checks to see if an element is able to be clicked
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    private static void isElementClickable(WebDriver driver, By element, String str) {

        try {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.TIMEOUT));
            wait.until(ExpectedConditions.elementToBeClickable(element));

        } catch (Exception e) {

            fail(str + " was not in a clickable state.\n");

        } // end try-catch

    } // end isElementClickable()

    /**
     * Checks to see if an element is present on a web page within 60 seconds.
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    public static void isElementPresent(WebDriver driver, By element, String str) {

        try {

            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(Constants.TIMEOUT))
                    .ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            wait.until(ExpectedConditions.presenceOfElementLocated(element));

        } catch (Exception e) {

            fail(str + " was not present in specified time.\n");

        } // end try-catch

    } // end isElementPresent()

    /**
     * Navigates to a given URL
     *
     * @param driver      WebDriver
     * @param propertyURL Property name of the URL as a String
     */
    public static void navigate(WebDriver driver, String propertyURL) {

        try {

            driver.get(propFile.properties.getProperty(propertyURL));

        } catch (Exception e) {

            fail("Could not navigate to: " + propertyURL + "\n");

        } // end try-catch

    } // end navigate()

    /**
     * Returns the OS the tests are executing on to include in the report
     *
     * @return string OS as a string
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
        } // end if-else statement

        return strOS;
    } // end osInfo()

    /**
     * Captures a screenshot of the page content that is within focus
     * during execution. This is useful for when pages are smaller.
     *
     * @param driver WebDriver
     */
    public static void partialScreenshot(WebDriver driver) {

        try {

            final byte[] partialCapture = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Hooks.scenario.get().attach(partialCapture, "image/png", "Click to view screenshot");

        } catch (Exception e) {

            fail("Could not capture a partial screenshot. Error: " + e.getMessage() + "\n");

        } // end try catch

    } // end partialScreenshot()

    /**
     * Pauses script execution for a desired amount of seconds.
     * This method should only be used if Explicit waits are not working.
     *
     * @param seconds The amount of seconds to wait for. This accepts fractions of seconds as well.
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
     * @param driver WebDriver
     */
    public static void screenshot(WebDriver driver) {

        BufferedImage img = CaptureScreenshot.getScreenshot(driver);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {

            ImageIO.write(img, "png", outStream);
            outStream.flush();
            byte[] imgInBytes = outStream.toByteArray();
            outStream.close();
            Hooks.scenario.get().attach(imgInBytes, "image/png", "Click to view screenshot");

        } catch (Exception e) {

            System.out.println("Could not capture a screenshot of the entire page. Attempting to capture a partial screenshot...\n");
            partialScreenshot(driver);

        } // end outer try catch

    } // end screenshot()

    /**
     * Select an option from a dropdown list based off the order it appears
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param index   The number in the list where the desired option is
     * @param str     String of WebElement for assert message
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

    } // end selectDropdownOptionByIndex()

    /**
     * Switches to a visible iFrame based on a given locator
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    public static void switchiFrame(WebDriver driver, By element, String str) {

        isElementPresent(driver, element, str);

        try {

            driver.switchTo().frame(driver.findElement(element));

        } catch (Exception e) {

            fail("Could not switch to iFrame: " + str);

        } // end try-catch

        pauseForSeconds(1);

    } // end switchiFrame()

    /**
     * Returns a previously highlighted element using the highlightElement()
     * method back to its original css style
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    public static void unhighlightElement(WebDriver driver, By element, String str) {

        isElementPresent(driver, element, str);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement ele = driver.findElement(element);

        try {

            js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]", ele, "style", ogStyle);

        } catch (Exception e) {

            fail("Unable to remove highlight from element: " + str + "\n");

        } // end try-catch

    } // end unhighlightElement()

} // end CommonMethods.java