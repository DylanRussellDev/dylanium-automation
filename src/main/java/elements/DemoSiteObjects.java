package elements;

import org.openqa.selenium.By;

public class DemoSiteObjects {

    // Replace LINK_TEXT with desired option
    public static String lnkAvailableExamples = "//ul//a[contains(text(), 'LINK_TEXT')]";

    // TinyMCE WYSIWYG Editor
    public static By iframeWYSIWYG = By.id("mce_0_ifr");
    public static By txtTinyMCE = By.id("tinymce");

    // Form Authentication
    public static By txtUsername = By.id("username");
    public static By txtPassword = By.id("password");
    public static By btnLogin = By.xpath("//button[@type='submit']");
    public static By msgSuccessLogin = By.xpath("//div[@class='flash success']");
}
