/*
 * Filename: CommonMethods.java
 * Author: Dylan Russell
 * Purpose: Commonly used methods during test execution.
 */

package io.github.dylanrusselldev.utilities.core;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.openqa.selenium.By;
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
import org.slf4j.event.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.time.Duration;
import java.util.Arrays;

public class CommonMethods {

    private static final ReadConfigFile propFile = new ReadConfigFile();

    private static final LoggerClass LOGGER = new LoggerClass(CommonMethods.class);

    /**
     * Blurs an element via it's CSS property
     *
     * @param driver  WebDriver
     * @param element The WebElement identifier
     * @param str     String of WebElement for assert message
     */
    public static void blurElement(WebDriver driver, By element, String str) {
        isElementPresent(driver, element, str);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement ele = driver.findElement(element);

        try {

            js.executeScript("arguments[0].setAttribute('style', 'filter: blur(0.25rem);');", ele);

        } catch (Exception e) {

            LOGGER.logAndFail("Could not blur out " + str, e);

        } // end try-catch

    } // end blurElement()



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

                LOGGER.logAndFail("Could not click on element: " + str + " because it became detached from the DOM structure", e);

            } catch (StaleElementReferenceException s) {

                LOGGER.logAndFail("Could not click on element: " + str + " because another element was concealing it", s);

            } catch (TimeoutException t) {

                LOGGER.logAndFail("After clicking on: " + str + ", the page took too long to load", t);

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

        try {

            decryptor.setAlgorithm(propFile.properties.getProperty("algorithm"));
            decryptor.setPassword(propFile.properties.getProperty("secretPass"));
            decryptor.setIvGenerator(new RandomIvGenerator());
            decryptor.setKeyObtentionIterations(Integer.parseInt(propFile.properties.getProperty("keyIterations")));

            return decryptor.decrypt(propFile.properties.getProperty(property));

        } catch (Exception e) {

            LOGGER.logAndFail("Error encountered when trying to decrypt the information", e);
            return null;

        } // end try-catch

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

            LOGGER.logAndFail("Could not get text from: " + str + "\n");
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

            LOGGER.logAndFail("There were no files found in the folder path: " + folderPath);

        } // end if-else

        return newestFile;
    } // end getNewestFile()

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

        try {

            js.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;'):", ele);

        } catch (Exception e) {

            LOGGER.logAndFail("Unable to highlight element: " + str, e);

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

            LOGGER.logAndFail(str + " could not be hovered over", e);

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

            LOGGER.logAndFail(str + " could not be hovered over", e);

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
    public static void enterText(WebDriver driver, By element, String input, String str) {

        isElementPresent(driver, element, str);

        try {

            driver.findElement(element).clear();
            driver.findElement(element).sendKeys(input);

        } catch (Exception e) {

            LOGGER.logAndFail("Could not input text '" + input + "' into " + str, e);

        } // end try-catch

    } // end enterText()

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

            LOGGER.logAndFail(str + " was not in a clickable state", e);

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

            LOGGER.logAndFail(str + " was not present on the page within " + Constants.TIMEOUT + " seconds", e);

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

            LOGGER.logAndFail("Could not navigate to " + propertyURL, e);

        } // end try-catch

    } // end navigate()

    /**
     * Captures a screenshot of the page content that is within focus
     * during execution. This is useful for when pages are smaller.
     *
     * @param driver WebDriver
     */
    public static void partialScreenshot(WebDriver driver) {

        try {

            final byte[] partialCapture = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Hooks.getScenario().attach(partialCapture, "image/png", "Click to view screenshot");
        } catch (Exception e) {

            LOGGER.logAndFail("Could not capture a partial screenshot", e);

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

            LOGGER.logAndFail("Unable to pause execution for " + seconds + " seconds", e);

        } // end try-catch

    } // end pauseForSeconds()

    /**
     * Takes a screenshot and embed its in the report
     *
     * @param driver WebDriver
     * @param captureType   How much of the page to capture
     */
    public static void screenshot(WebDriver driver, Capture captureType) {
        try {

            BufferedImage image = Shutterbug.shootPage(driver, captureType, true).getImage();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outStream);
            outStream.flush();
            byte[] imgInBytes = outStream.toByteArray();
            outStream.close();
            Hooks.getScenario().attach(imgInBytes, "image/png", "Screenshot");
        } catch (Exception e) {

            LOGGER.log(Level.WARN, "Could not capture a screenshot of the page using Shutterbug. " +
                    "Attempting to capture an in-view screenshot with Selenium...", e);
            partialScreenshot(driver);

        } // end try-catch

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

                LOGGER.logAndFail(str + " could not be clicked because another element was concealing it", e);

            } catch (NoSuchElementException n) {

                LOGGER.logAndFail("There is no option listed in the " + str + " at position " + index, n);

            } catch (StaleElementReferenceException s) {

                LOGGER.logAndFail(str + " became detached from the DOM when trying to interact with it", s);

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

            LOGGER.logAndFail("Could not switch to iFrame: " + str, e);

        } // end try-catch

        pauseForSeconds(1);

    } // end switchiFrame()

} // end CommonMethods.java