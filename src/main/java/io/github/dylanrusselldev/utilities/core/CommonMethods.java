package io.github.dylanrusselldev.utilities.core;

import io.github.dylanrusselldev.steps.Hooks;
import io.github.dylanrusselldev.utilities.filereaders.ReadConfigFile;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import io.github.dylanrusselldev.utilities.reporting.CaptureScreenshot;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.time.Duration;
import java.util.Arrays;

/*
 * Filename: CommonMethods.java
 * Author: Dylan Russell
 * Purpose: Commonly used methods during test execution.
 */
public class CommonMethods {

    private static final LoggerClass LOGGER = new LoggerClass(CommonMethods.class);
    private static final ReadConfigFile readConfigFile = new ReadConfigFile(Constants.PROP_FILEPATH + "\\automation.properties");

    /**
     * Blurs an element via it's CSS property.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static void blurElement(WebDriver driver, By element, String str) {
        isElementPresent(driver, element, str);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement ele = driver.findElement(element);

        try {
            js.executeScript("arguments[0].setAttribute('style', 'filter: blur(0.25rem);');", ele);
        } catch (Exception e) {
            LOGGER.fail("Could not blur out " + str, e);
        }

    }

    /**
     * Find an element and perform a click function on it.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static void click(WebDriver driver, By element, String str) {
        isElementPresent(driver, element, str);
        isElementClickable(driver, element, str);

        try {
            driver.findElement(element).click();
            LOGGER.info("Clicked on: " + str);
        } catch (ElementClickInterceptedException e) {
            LOGGER.fail("Unable to click on the '" + str + "' because it became detached from the DOM structure", e);
        } catch (StaleElementReferenceException s) {
            LOGGER.fail("Unable to click on the '" + str + "' because the DOM changed", s);
        } catch (TimeoutException t) {
            LOGGER.fail("After clicking on the '" + str + "', the page took too long to load", t);
        }

    }

    /**
     * Decrypts an encrypted string. Pass the property name of the string to decrypt.
     *
     * @param property the property name of the string to decrypt
     */
    public static String decrypt(String property) {
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();

        try {
            decryptor.setAlgorithm(readConfigFile.properties.getProperty("algorithm"));
            decryptor.setPassword(readConfigFile.properties.getProperty("secretPass"));
            decryptor.setIvGenerator(new RandomIvGenerator());
            decryptor.setKeyObtentionIterations(Integer.parseInt(readConfigFile.properties.getProperty("keyIterations")));
            return decryptor.decrypt(readConfigFile.properties.getProperty(property));
        } catch (Exception e) {
            LOGGER.fail("Error encountered when trying to decrypt the information", e);
            return null;
        }

    }

    /**
     * Find an element and input text into it.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param input   the string to input into the WebElement
     * @param str     the WebElement type for the log message
     */
    public static void enterText(WebDriver driver, By element, String input, String str) {
        isElementPresent(driver, element, str);

        try {
            driver.findElement(element).clear();
            driver.findElement(element).sendKeys(input);
        } catch (Exception e) {
            LOGGER.fail("Could not input text: " + input + " into " + str, e);
        }

    }

    /**
     * Find an element and return the text in it. If the .getText() attempt fails,
     * the script will attempt to find the "value" attribute.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static String getElementText(WebDriver driver, By element, String str) {
        isElementPresent(driver, element, str);

        try {
            String val = driver.findElement(element).getText();

            if (val.isEmpty()) {
                val = driver.findElement(element).getAttribute("value");
            }

            LOGGER.info("Returning text value: " + val);

            return val;
        } catch (Exception e) {
            LOGGER.fail("Could not get text from: " + str + "\n");
            return null;
        }

    }

    /**
     * Returns a file based off the filename.
     *
     * @param driver WebDriver
     * @param filename the filename
     */
    public static File retrieveFile(WebDriver driver, String filename) {

        try {
            File file = new File(Constants.TARGET_FILE_DOWNLOADS + "\\" + Hooks.getScenario().getName() + "\\" + filename);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.TIMEOUT));
            wait.until(x -> file.exists());
            LOGGER.info("Located the " + filename + " file");
            return file;
        } catch (Exception e) {
            LOGGER.fail("Unable to locate the file: " + filename + " in folder location: " +
                    Constants.TARGET_FILE_DOWNLOADS + "\\" + Hooks.getScenario().getName() + "\\" + filename);
            return null;
        }

    }

    /**
     * Returns the most recent file in a folder location with a given file extension.
     *
     * @param folderPath the folder location
     * @param ext        the filename extension to look for
     */
    public static File getNewestFile(String folderPath, String ext) {
        File newestFile = null;
        FileFilter fileFilter = new WildcardFileFilter("*." + ext);
        File directory = new File(folderPath);
        File[] filesArray;

        for (int i = 0; i < 5; i++) {
            filesArray = directory.listFiles(fileFilter);

            if (filesArray != null && filesArray.length > 0) {
                Arrays.sort(filesArray, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

                if (filesArray[0].getName().endsWith(".crdownload")) {
                    LOGGER.info("The file is currently downloading...");
                    CommonMethods.pauseForSeconds(5);
                } else {
                    newestFile = filesArray[0];
                    LOGGER.info("The newest file in the folder is: " + newestFile.getName());
                    break;
                }

            } else {
                LOGGER.warn("No files found in the folder path: " + folderPath + ". Checking again in a moment.");
                CommonMethods.pauseForSeconds(5);
            }

        }

        return newestFile;
    }

    /**
     * Highlights an element in yellow with a red outline around it.
     * This makes monitoring and debugging multiple validations much easier.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static void highlightElement(WebDriver driver, By element, String str) {
        isElementPresent(driver, element, str);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement ele = driver.findElement(element);

        try {
            js.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;'):", ele);
        } catch (Exception e) {
            LOGGER.fail("Unable to highlight element: " + str, e);
        }

    }

    /**
     * Find an element and hover on the element using the JavaScript function.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static void hoverJavascript(WebDriver driver, By element, String str) {
        isElementPresent(driver, element, str);

        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].onmouseover()", driver.findElement(element));
        } catch (Exception e) {
            LOGGER.fail(str + " could not be hovered over", e);
        }

    }

    /**
     * Find an element and hover on the element using the Selenium function.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static void hoverSelenium(WebDriver driver, By element, String str) {
        isElementPresent(driver, element, str);

        try {
            Actions action = new Actions(driver);
            action.moveToElement(driver.findElement(element)).perform();
        } catch (Exception e) {
            LOGGER.fail(str + " could not be hovered over", e);
        }

    }

    /**
     * Checks if an element can be clicked on.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    private static void isElementClickable(WebDriver driver, By element, String str) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.TIMEOUT));
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
        } catch (Exception e) {
            LOGGER.fail(str + " was not in a clickable state", e);
        }

    }

    /**
     * Checks to see if an element is present on a web page within 60 seconds.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static void isElementPresent(WebDriver driver, By element, String str) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(Constants.TIMEOUT))
                .ignoring(StaleElementReferenceException.class);

        try {
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(element)));
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(element)));
        } catch (Exception e) {
            LOGGER.fail(str + " was not present on the page within " + Constants.TIMEOUT + " seconds", e);
        }

    }

    /**
     * Navigate to a website using the URL defined in the properties file.
     *
     * @param driver      WebDriver
     * @param propertyURL the property name of the URL as a String
     */
    public static void navigate(WebDriver driver, String propertyURL) {

        try {
            driver.get(readConfigFile.properties.getProperty(propertyURL));
            LOGGER.info("Navigating to URL " + readConfigFile.properties.getProperty(propertyURL));
        } catch (Exception e) {
            LOGGER.fail("Could not navigate to " + propertyURL, e);
        }

    }

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
            LOGGER.fail("Could not capture a partial screenshot", e);
        }

    }

    /**
     * Pauses script execution for a desired amount of seconds.
     * This method should only be used if Explicit waits are not working.
     *
     * @param seconds the amount of seconds to wait for. This accepts fractions of seconds as well.
     */
    public static void pauseForSeconds(double seconds) {
        double milli = seconds * 1000;
        int intMilli = (int) milli;

        try {
            Thread.sleep(intMilli);
        } catch (InterruptedException e) {
            LOGGER.fail("Unable to pause execution for " + seconds + " seconds", e);
        }

    }

    /**
     * Takes a screenshot and embed it in the report.
     *
     * @param driver WebDriver
     */
    public static void fullScreenshot(WebDriver driver) {

        try {
            BufferedImage image = CaptureScreenshot.takeScreenshot(driver);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outStream);
            outStream.flush();
            byte[] imgInBytes = outStream.toByteArray();
            outStream.close();
            Hooks.getScenario().attach(imgInBytes, "image/png", "Screenshot");
        } catch (Exception e) {
            LOGGER.warn("Could not capture a screenshot of the page. " +
                    "Attempting to capture an in-view screenshot...", e);
            partialScreenshot(driver);
        }

    }

    /**
     * Select an option from a dropdown list based off the order it appears.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param index   the number in the list where the desired option is
     * @param str     the WebElement type for the log message
     */
    public static void selectDropdownOptionByIndex(WebDriver driver, By element, int index, String str) {
        isElementPresent(driver, element, str);
        Select select = new Select(driver.findElement(element));

        try {
            select.selectByIndex(index);
        } catch (ElementClickInterceptedException e) {
            LOGGER.fail(str + " could not be clicked because another element was concealing it", e);
        } catch (NoSuchElementException n) {
            LOGGER.fail("There is no option listed in the " + str + " at position " + index, n);
        } catch (StaleElementReferenceException s) {
            LOGGER.fail(str + " became detached from the DOM when trying to interact with it", s);
        }

    }

    /**
     * Switches to a visible iFrame based on a given locator.
     *
     * @param driver  WebDriver
     * @param element the WebElement identifier
     * @param str     the WebElement type for the log message
     */
    public static void switchiFrame(WebDriver driver, By element, String str) {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(Constants.TIMEOUT))
                .ignoring(StaleElementReferenceException.class);
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
        } catch (Exception e) {
            LOGGER.fail("Could not switch to iFrame: " + str, e);
        }

        pauseForSeconds(1);
    }

}
