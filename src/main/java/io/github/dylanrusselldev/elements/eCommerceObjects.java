package io.github.dylanrusselldev.elements;

import org.openqa.selenium.By;

public class eCommerceObjects {

    public static By ddlLaptopsNotebooks = By.xpath("//a[text()='Laptops & Notebooks']");
    public static By lnkShowAllLaptops = By.xpath("//a[text()='Show All Laptops & Notebooks']");

    // Compare buttons
    public static By btnCompareMacBookAir = By.xpath("//a[text()='MacBook Air']//parent::h4//parent::div//parent::div" +
            "//button[@data-original-title='Compare this Product']");

    public static By btnCompareMacBookPro = By.xpath("//a[text()='MacBook Pro']//parent::h4//parent::div//parent::div" +
            "//button[@data-original-title='Compare this Product']");

    public static By msgSuccessProductComparison = By.xpath("//div[@class='alert alert-success alert-dismissible']");

    public static By lnkProductCompare = By.id("compare-total");

    public static By lblMacBookAir = By.xpath("//tr//strong[text()='MacBook Air']");
    public static By lblMacBookPro = By.xpath("//tr//strong[text()='MacBook Pro']");
}
