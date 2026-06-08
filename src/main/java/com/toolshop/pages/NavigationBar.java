package com.toolshop.pages;

import com.toolshop.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * NavigationBar — Page Object for the global navigation bar.
 */
public class NavigationBar {

    private final WebDriver driver;

    private final By homePageBtn    = By.xpath("//a[text()='Home']");
    private final By categoriesBtn  = By.cssSelector(
            "#navbarSupportedContent > ul > li:nth-child(2) > button");
    private final By signInBtn      = By.cssSelector(
            "#navbarSupportedContent > ul > li:nth-child(4) > a");
    private final By handToolsLink  = By.xpath("//a[text()='Hand Tools']");
    private final By powerToolsLink = By.xpath("//a[text()='Power Tools']");
    private final By cartNavLink    = By.cssSelector("[data-test='nav-cart']");

    public NavigationBar(WebDriver driver) {
        this.driver = driver;
    }

    public void clickCategoriesLink() {
        WaitUtils.waitForClickable(driver, categoriesBtn).click();
    }

    public void clickLoginLink() {
        WaitUtils.waitForClickable(driver, signInBtn).click();
    }

    public void clickHomePageLink() {
        WaitUtils.waitForClickable(driver, homePageBtn).click();
    }

    public void navigateToHandTools() {
        clickCategoriesLink();
        WaitUtils.waitForClickable(driver, handToolsLink).click();
    }

    public void navigateToPowerTools() {
        clickCategoriesLink();
        WaitUtils.waitForClickable(driver, powerToolsLink).click();
    }

    public void clickCartLink() {
        WaitUtils.waitForClickable(driver, cartNavLink).click();
    }
}
