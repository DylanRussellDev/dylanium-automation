/*
 * Filename: TestObjects.java
 * Author: Dylan Russell
 * Purpose: A file to hold element identifiers for a page.
 */

package pagesElements;

import org.openqa.selenium.By;

public class TestObjects {

	public static By btn2 = By.xpath("//span[@onclick='r(2)']");
	public static By btnPlus = By.xpath("//span[text()=\"+\"]");
	public static By btnEquals = By.xpath("//span[text()=\"=\"]");
	public static By txtOutput = By.id("sciOutPut");
}