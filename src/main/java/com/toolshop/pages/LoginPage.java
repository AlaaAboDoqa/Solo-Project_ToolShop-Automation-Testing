package com.toolshop.pages;

import com.toolshop.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;

    private final By emailInput    = By.cssSelector("[data-test='email']");
    private final By passwordInput = By.cssSelector("[data-test='password']");
    private final By loginBtn      = By.cssSelector("[data-test='login-submit']");
    private final By emailErrorMessage = By.cssSelector("[data-test='email-error']");
    private final By passErrorMessage  = By.cssSelector("[data-test='login-error'], .alert-danger");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String email, String password) {
        WaitUtils.waitForVisible(driver, emailInput).clear();
        driver.findElement(emailInput).sendKeys(email);
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        WaitUtils.waitForClickable(driver, loginBtn).click();
    }

    public void loginAs(String email, String password) {
        login(email, password);
        clickLogin();
    }

    public boolean isEmailErrorDisplayed() {
        try {
            return WaitUtils.waitForVisible(driver, emailErrorMessage,
                    WaitUtils.SHORT_WAIT_SEC).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPasswordErrorDisplayed() {
        try {
            return WaitUtils.waitForVisible(driver, passErrorMessage,
                    WaitUtils.SHORT_WAIT_SEC).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
