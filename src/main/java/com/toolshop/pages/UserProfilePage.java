package com.toolshop.pages;

import com.toolshop.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserProfilePage {

    private final WebDriver driver;

    // Locators
    private final By profileBtn     = By.xpath("//a[@data-test=\"nav-profile\"]");
    private final By firstNameInput = By.cssSelector("[data-test='first-name']");
    private final By lastNameInput  = By.cssSelector("[data-test='last-name']");
    private final By emailInput     = By.cssSelector("[data-test='email']");
    private final By phoneInput     = By.cssSelector("[data-test='phone']");
    private final By dobInput       = By.cssSelector("[data-test='dob']");
    private final By addressInput   = By.cssSelector("[data-test='address']");
    private final By cityInput      = By.cssSelector("[data-test='city']");
    private final By postCodeInput  = By.cssSelector("[data-test='postcode']");

    // FIX 3: removed trailing space from button text — "Update Profile " → "Update Profile"
    // Using normalize-space() makes the locator resilient to any whitespace variation.
    private final By updateProfileBtn = By.cssSelector("[data-test='update-profile-submit']");

    private final By firstNameError = By.cssSelector("[data-test='first-name-error']");
    private final By lastNameError  = By.cssSelector("[data-test='last-name-error']");

    // FIX 4: unified toast selector — try the most specific one first,
    // then fall back. The original had two separate try/catch blocks each
    // starting their own wait, so if the first selector missed the toast
    // (e.g. it appeared briefly), the second wait started too late.
    private final By toastBody      = By.cssSelector(".toast-body, .alert, ngb-toast, "
                                                    + ".toast, [role='alert'], .ngb-toasts .toast");

    public UserProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    // =====================================================================
    // READ — get current field values
    // =====================================================================

    public String getFirstName() {
        return WaitUtils.waitForVisible(driver, firstNameInput, WaitUtils.NAVIGATION_WAIT_SEC)
                .getAttribute("value").trim();
    }

    public String getLastName() {
        return WaitUtils.waitForVisible(driver, lastNameInput, WaitUtils.NAVIGATION_WAIT_SEC)
                .getAttribute("value").trim();
    }

    /** Returns the email shown on the profile (may be read-only). */
    public String getEmail() {
        return WaitUtils.waitForVisible(driver, emailInput, WaitUtils.NAVIGATION_WAIT_SEC)
                .getAttribute("value").trim();
    }

    public String getPhone() {
        return WaitUtils.waitForVisible(driver, phoneInput, WaitUtils.NAVIGATION_WAIT_SEC)
                .getAttribute("value").trim();
    }

    // =====================================================================
    // NAVIGATE
    // =====================================================================

    public void clickProfileBtn() {
        driver.findElement(profileBtn).click();
    }

    // =====================================================================
    // WRITE — update field values
    // =====================================================================

    public void setFirstName(String firstName) {
        WebElement input = WaitUtils.waitForClickable(driver, firstNameInput);
        input.clear();
        input.sendKeys(firstName);
    }

    public void setLastName(String lastName) {
        WebElement input = WaitUtils.waitForClickable(driver, lastNameInput);
        input.clear();
        input.sendKeys(lastName);
    }

    public void setPhone(String phone) {
        WebElement input = WaitUtils.waitForClickable(driver, phoneInput);
        input.clear();
        input.sendKeys(phone);
    }

    public void clickUpdateProfile() {
        WaitUtils.waitForClickable(driver, updateProfileBtn).click();
    }

    // =====================================================================
    // CONVENIENCE — combined update operations
    // =====================================================================

    public void updateFullName(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
        clickUpdateProfile();
    }

    public void submitWithBlankFirstName() {
        setFirstName("");
        clickUpdateProfile();
    }

    public void submitWithBlankLastName() {
        setLastName("");
        clickUpdateProfile();
    }

    // =====================================================================
    // ASSERTIONS SUPPORT — toast and validation errors
    // =====================================================================

    /**
     * FIX 4: Single combined selector with one wait call.
     * The original had two sequential try/catch waits — if the toast appeared
     * and disappeared during the first wait's timeout, the second wait always
     * returned "". Now a single wait covers all toast variants at once.
     */
    public String getToastMessage() {
        try {
            return WaitUtils.waitForVisible(driver, toastBody, WaitUtils.TOAST_WAIT_SEC)
                    .getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * FIX 2: Use waitForVisible with a short timeout instead of isElementPresent.
     * isElementPresent does a zero-wait DOM check — the validation error element
     * appears asynchronously after the button click, so it was always missed.
     */
    public boolean isFirstNameErrorDisplayed() {
        try {
            return WaitUtils.waitForVisible(driver, firstNameError, WaitUtils.SHORT_WAIT_SEC)
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLastNameErrorDisplayed() {
        try {
            return WaitUtils.waitForVisible(driver, lastNameError, WaitUtils.SHORT_WAIT_SEC)
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstNameErrorText() {
        try {
            return WaitUtils.waitForVisible(driver, firstNameError, WaitUtils.SHORT_WAIT_SEC)
                    .getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getLastNameErrorText() {
        try {
            return WaitUtils.waitForVisible(driver, lastNameError, WaitUtils.SHORT_WAIT_SEC)
                    .getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}