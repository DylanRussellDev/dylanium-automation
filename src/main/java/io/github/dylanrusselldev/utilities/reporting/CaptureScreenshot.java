package io.github.dylanrusselldev.utilities.reporting;

import io.github.dylanrusselldev.utilities.core.CommonMethods;
import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/*
 * Filename: CaptureScreenshot.java
 * Author: Dylan Russell
 * Purpose: Captures a screenshot of the entire webpage.
 */
public class CaptureScreenshot {

    private static final LoggerClass LOGGER = new LoggerClass(CaptureScreenshot.class);

    private static int getFullPageHeight(WebDriver driver) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        return Integer.parseInt(javascriptExecutor.executeScript("return document.body.scrollHeight").toString());
    }

    private static int getFullPageWidth(WebDriver driver) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        return ((Long) javascriptExecutor.executeScript("return window.innerWidth", new Object[0])).intValue();
    }

    private static int getWindowHeight(WebDriver driver) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        return ((Long) javascriptExecutor.executeScript("return window.innerHeight", new Object[0])).intValue();
    }

    // Take a screenshot of what is currently in the browser window
    private static BufferedImage screenshotCurrentView(WebDriver wd) {
        ByteArrayInputStream bais = null;
        TakesScreenshot takesScreenshot = (TakesScreenshot) new Augmenter().augment(wd);

        try {
            bais = new ByteArrayInputStream(takesScreenshot.getScreenshotAs(OutputType.BYTES));
            return ImageIO.read(bais);
        } catch (IOException e) {
            LOGGER.logAndFail("Unable to parse screenshot", e);
            return null;
        } finally {

            try {

                if (bais != null) {
                    bais.close();
                }

            } catch (IOException i) {
                LOGGER.logAndFail("Unable to close ByteArrayInputStream", i);
            }

        }

    }

    public static BufferedImage takeScreenshot(WebDriver driver) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

        int fullHeight = getFullPageHeight(driver);
        int fullWidth = getFullPageWidth(driver);
        int pageHeight = getWindowHeight(driver);
        int scrollTimes = fullHeight / pageHeight;
        int tail = fullHeight - pageHeight * scrollTimes;

        BufferedImage finalScreenshot = new BufferedImage(fullWidth, fullHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = finalScreenshot.createGraphics();

        for (int i = 0; i < scrollTimes; i++) {
            javascriptExecutor.executeScript("scrollTo(0, arguments[0])", pageHeight * i);
            CommonMethods.pauseForSeconds(0.5);
            BufferedImage partialImage = screenshotCurrentView(driver);
            graphics.drawImage(partialImage, 0, i * pageHeight, null);
        }

        if (tail > 0) {
            javascriptExecutor.executeScript("scrollTo(0, document.body.scrollHeight)");
            CommonMethods.pauseForSeconds(0.5);
            BufferedImage last = screenshotCurrentView(driver);
            BufferedImage tailImage = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
            graphics.drawImage(tailImage, 0, scrollTimes * pageHeight, null);
        }

        graphics.dispose();
        return finalScreenshot;

    }

}
