/*
 * Filename: CaptureScreenshot.java
 * Author: Dylan Russell
 * Purpose: Takes a screenshot of a full web page and embeds it in the cucumber report.
 */

package utilities;

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

	private static int scrollTime = 0;
	
	public CaptureScreenshot(int time) {
		scrollTime = time;
	}
	
	private static String getHeight(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript("return document.body.scrollHeight").toString();
	}
	
	private static int getWidth(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return ((Long) js.executeScript("return window.innerWidth", new Object[0])).intValue();
	}
	
	private static int getWindowHeight(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return ((Long) js.executeScript("return window.innerHeight", new Object[0])).intValue();
	}
	
	private static void waitForScrolling() {
		try {
			Thread.sleep(scrollTime);
		} catch (InterruptedException ignored) {
			// Ignored exception
		} // end try-catch
	}
	
	private static BufferedImage getNativeScreenshot(WebDriver wd) {
		ByteArrayInputStream imageArrayStream = null;
		TakesScreenshot takesScreenshot = (TakesScreenshot) new Augmenter().augment(wd);
		try {
			imageArrayStream = new ByteArrayInputStream(takesScreenshot.getScreenshotAs(OutputType.BYTES));
			return ImageIO.read(imageArrayStream);
		} catch (IOException e) {
			throw new RuntimeException("Can not parse screenshot data", e);
		} finally {
			try {
				if (imageArrayStream != null) {
					imageArrayStream.close();
				} // end if
			} catch (IOException ignored) {
				// Ignored exception
			} // end inner try-catch
		} // end try-catch-finally
	} // end getNativeScreenshot
	
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
			waitForScrolling();
			BufferedImage last = getNativeScreenshot(wd);
			graphics.drawImage(last, 0, i * winH, null);
		} // end for
		
		if (tail > 0) {
			js.executeScript("scrollTo(0, document.body.scrollHeight)");
			waitForScrolling();
			BufferedImage last = getNativeScreenshot(wd);
			BufferedImage tailImg = last.getSubimage(0, last.getHeight() - tail, last.getWidth(), tail);
			graphics.drawImage(tailImg, 0, timeScroll * winH, null);
		} // end if		
		
		graphics.dispose();
		return finalImg;
	} // end getScreenshot
} // end class