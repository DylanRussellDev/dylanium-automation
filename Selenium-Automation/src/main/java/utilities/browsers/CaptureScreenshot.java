/*
 * Filename: CaptureScreenshot.java
 * Purpose: Takes a screenshot of a full web page and embeds it in the execution report.
 */

package utilities.browsers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public class CaptureScreenshot {
	
	private static String getHeight(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript("return document.body.scrollHeight").toString();
	} // end getHeight()
	
	private static int getWidth(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return ((Long) js.executeScript("return window.innerWidth", new Object[0])).intValue();
	} // end getWidth()
	
	private static int getWindowHeight(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return ((Long) js.executeScript("return window.innerHeight", new Object[0])).intValue();
	} // end getWindowHeight()
	
	private static BufferedImage getNativeScreenshot(WebDriver wd) {
		TakesScreenshot takesScreenshot = (TakesScreenshot) new Augmenter().augment(wd);

		try (ByteArrayInputStream imageArrayStream = new ByteArrayInputStream(takesScreenshot.getScreenshotAs(OutputType.BYTES))) {
			return ImageIO.read(imageArrayStream);
		} catch (IOException e) {
			throw new RuntimeException("Can not parse screenshot data. Error: " + e.getMessage() + "\n");
		} // end try catch

	} // end getNativeScreenshot()
	
	public static BufferedImage getScreenshot(WebDriver wd) {
		JavascriptExecutor js = (JavascriptExecutor) wd;

		int allH = Integer.parseInt(getHeight(wd));
		int allW = getWidth(wd);
		int winH = getWindowHeight(wd);
		int timeScroll = allH / winH;
		int tail = allH - winH * timeScroll;

		BufferedImage finalImg = new BufferedImage(allW, allH, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D graphics = finalImg.createGraphics();
		
		for (int i = 0; i < timeScroll; i++) {
			js.executeScript("scrollTo(0, arguments[0])", winH * i);
			BufferedImage last = getNativeScreenshot(wd);
			graphics.drawImage(last, 0, i * winH, null);
		} // end for
		
		if (tail > 0) {
			js.executeScript("scrollTo(0, document.body.scrollHeight)");
			BufferedImage last = getNativeScreenshot(wd);
			BufferedImage tailImg = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
			graphics.drawImage(tailImg, 0, timeScroll * winH, null);
		} // end if		
		
		graphics.dispose();
		return finalImg;

	} // end getScreenshot()

} // end CaptureScreenshot.java
