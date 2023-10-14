package io.github.dylanrusselldev.utilities.api;

import io.github.dylanrusselldev.utilities.logging.LoggerClass;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

/*
 * Filename: CookieHelper.java
 * Author: Dylan Russell
 * Purpose: Provides methods that enable working with Cookies during test execution.
 */
public class CookieHelper {

    private static final LoggerClass LOGGER = new LoggerClass(CookieHelper.class);

    public static void addCookie(WebDriver driver, String key, String value) {
        LOGGER.info("Adding the following cookie; Key: " + key + ", value: " + value);
        driver.manage().addCookie(new Cookie(key, value));
    }

    public static Set<Cookie> getAllCookies(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        LOGGER.info("Returning cookies: " + cookies);
        return cookies;
    }

    public static Cookie getCookie(WebDriver driver, String cookieName) {
        Cookie cookie = driver.manage().getCookieNamed(cookieName);
        LOGGER.info("Returning the following cookie: " + cookie);
        return cookie;
    }

    public static String getCookieValue(WebDriver driver, String cookieName) {
        String value = driver.manage().getCookieNamed(cookieName).getValue();
        LOGGER.info("Returning the value from the " + cookieName + " cookie: " + value);
        return value;
    }

}
