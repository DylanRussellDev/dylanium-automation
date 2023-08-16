package io.github.dylanrusselldev.webelements;

import org.openqa.selenium.By;

public class DemoSiteObjects {

    // Replace LINK_TEXT with desired option
    public static String lnkAvailableExamples = "//ul//a[contains(text(), 'LINK_TEXT')]";
    public static String lnkStatusCodes = "//a[text()=STATUS_CODE]";

    // TinyMCE WYSIWYG Editor
    public static final By iframeWYSIWYG = By.id("mce_0_ifr");
    public static final By txtTinyMCE = By.id("tinymce");

    // Form Authentication
    public static final By txtUsername = By.id("username");
    public static final By txtPassword = By.id("password");
    public static final By btnLogin = By.xpath("//button[@type='submit']");
    public static final By msgSuccessLogin = By.xpath("//div[@class='flash success']");

    // Status Codes
    public static final By lbl500Text = By.xpath("//p[contains(text(), '500 status code')]");

    // Inputs
    public static final By hdInputs = By.xpath("//h3[text()='Inputs']");
    public static final By txtNumbers = By.id("invalidID");

    // Add/Remove Elements
    public static final By hdAddRemoveElements = By.xpath("//h3[text()='Add/Remove Elements']");
    public static final By btnAddElement = By.xpath("//button[text()='Add Element']");
    public static final By btnDelete = By.xpath("//button[text()='Delete']");

    public static final By lnkSamplePDF = By.xpath("//a[@href='download/sample.pdf']");
}
