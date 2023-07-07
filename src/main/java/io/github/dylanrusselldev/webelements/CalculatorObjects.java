package io.github.dylanrusselldev.webelements;

import org.openqa.selenium.By;

public class CalculatorObjects {

    // Google

    public static final By imgGoogleLogo = By.xpath("//img[@alt='Google']");

    // Calculator.com

	public static final By btn2 = By.xpath("//span[@onclick='r(2)']");
	public static final By btnPlus = By.xpath("//span[text()=\"+\"]");
	public static final By btnEquals = By.xpath("//span[text()=\"=\"]");
	public static final By txtOutput = By.id("sciOutPut");

}
