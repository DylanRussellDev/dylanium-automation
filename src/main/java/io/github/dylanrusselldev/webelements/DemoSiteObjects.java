package io.github.dylanrusselldev.webelements;

import org.openqa.selenium.By;

public class DemoSiteObjects {

    // Replace LINK_TEXT with desired option
    public static String lnkAvailableExamples = "//ul//a[contains(text(), 'LINK_TEXT')]";
    public static String lnkStatusCodes = "//a[text()=STATUS_CODE]";

    // TinyMCE WYSIWYG Editor
    public static By iframeWYSIWYG = By.id("mce_0_ifr");
    public static By txtTinyMCE = By.id("tinymce");

    // Form Authentication
    public static By txtUsername = By.id("username");
    public static By txtPassword = By.id("password");
    public static By btnLogin = By.xpath("//button[@type='submit']");
    public static By msgSuccessLogin = By.xpath("//div[@class='flash success']");

    // Status Codes
    public static By lbl500Text = By.xpath("//p[contains(text(), '500 status code')]");

    // Inputs
    public static By hdInputs = By.xpath("//h3[text()='Inputs']");
    public static By txtNumbers = By.id("invalidID");

    // Add/Remove Elements
    public static By hdAddRemoveElements = By.xpath("//h3[text()='Add/Remove Elements']");
    public static By btnAddElement = By.xpath("//button[text()='Add Element']");
    public static By btnDelete = By.xpath("//button[text()='Delete']");

}
