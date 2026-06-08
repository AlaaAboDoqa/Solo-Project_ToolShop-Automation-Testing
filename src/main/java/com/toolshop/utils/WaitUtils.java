package com.toolshop.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtils — Centralized explicit wait utility.
 *
 * ALL wait durations are defined here as constants.
 * No Duration.ofSeconds() call should appear anywhere else in the framework.
 */
public class WaitUtils {

    // ─── Wait Duration Constants (seconds) ───────────────────────────────────
    /** Standard wait for most UI elements to appear or become clickable. */
    public static final long DEFAULT_WAIT_SEC        = 15;

    /** Short wait for inline error messages that appear immediately. */
    public static final long SHORT_WAIT_SEC          = 5;

    // Longer wait for heavy page loads (search results, category pages). 
    public static final long NAVIGATION_WAIT_SEC     = 20;

    /** Wait for the URL to change after a login redirect. */
    public static final long LOGIN_REDIRECT_WAIT_SEC = 15;

    /** Wait for toast/snackbar notifications to appear. */
    public static final long TOAST_WAIT_SEC          = 8;

    public static WebElement waitForVisible(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SEC))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisible(WebDriver driver, By locator, long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT_SEC))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebDriver driver, By locator, long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForUrlToNotContain(WebDriver driver, String urlFragment) {
        new WebDriverWait(driver, Duration.ofSeconds(LOGIN_REDIRECT_WAIT_SEC))
                .until(ExpectedConditions.not(
                        ExpectedConditions.urlContains(urlFragment)));
    }

    public static void waitForUrlToContain(WebDriver driver, String urlFragment) {
        new WebDriverWait(driver, Duration.ofSeconds(NAVIGATION_WAIT_SEC))
                .until(ExpectedConditions.urlContains(urlFragment));
    }

    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            waitForVisible(driver, locator, SHORT_WAIT_SEC);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
