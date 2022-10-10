package pageElements;

import org.openqa.selenium.By;

public class TestObjects {

    // Google
    public static By imgGoogleLogo = By.xpath("//img[@alt='Google']");

    // Calculator.com
	public static By btn2 = By.xpath("//span[@onclick='r(2)']");
	public static By btnPlus = By.xpath("//span[text()=\"+\"]");
	public static By btnEquals = By.xpath("//span[text()=\"=\"]");
	public static By txtOutput = By.id("sciOutPut");

} // end TestObjects.java
