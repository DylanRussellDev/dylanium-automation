package io.github.dylanrusselldev.webelements;

import org.openqa.selenium.By;

public class eCommerceObjects {

    public static final By ddlLaptopsNotebooks = By.xpath("//a[text()='Laptops & Notebooks']");
    public static final By lnkShowAllLaptops = By.xpath("//a[text()='Show All Laptops & Notebooks']");

    // Compare buttons
    public static final By btnCompareMacBookAir = By.xpath("//a[text()='MacBook Air']//parent::h4//parent::div//parent::div" +
            "//button[@data-original-title='Compare this Product']");

    public static final By btnCompareMacBookPro = By.xpath("//a[text()='MacBook Pro']//parent::h4//parent::div//parent::div" +
            "//button[@data-original-title='Compare this Product']");

    public static final By msgSuccessProductComparison = By.xpath("//div[@class='alert alert-success alert-dismissible']");

    public static final By lnkProductCompare = By.id("compare-total");

    public static final By lblMacBookAir = By.xpath("//tr//strong[text()='MacBook Air']");
    public static final By lblMacBookPro = By.xpath("//tr//strong[text()='MacBook Pro']");
}
