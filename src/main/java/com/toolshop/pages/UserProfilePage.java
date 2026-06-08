package com.toolshop.pages;

import com.toolshop.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class UserProfilePage {

	private final WebDriver driver;

	// Locators
	private final By profileBtn = By.xpath("//a[@data-test=\"nav-profile\"]");
	private final By firstNameInput = By.cssSelector("[data-test='first-name']");
	private final By lastNameInput = By.cssSelector("[data-test='last-name']");
	private final By emailInput = By.cssSelector("[data-test='email']");
	private final By phoneInput = By.cssSelector("[data-test='phone']");
	private final By dobInput = By.cssSelector("[data-test='dob']");
	private final By addressInput = By.cssSelector("[data-test='address']");
	private final By cityInput = By.cssSelector("[data-test='city']");
	private final By postCodeInput = By.cssSelector("[data-test='postcode']");
	private final By updateProfileBtn = By.xpath("[data-test='update-profile-btn']");
	private final By firstNameError = By.cssSelector("[data-test='first-name-error']");
	private final By lastNameError = By.cssSelector("[data-test='last-name-error']");
	private final By toastBody = By.cssSelector(".toast-body, .alert, ngb-toast");
	private final By toastContainer = By.cssSelector(".toast, [role='alert'], .ngb-toasts .toast");

	public UserProfilePage(WebDriver driver) {
		this.driver = driver;
	}

    // =============================
    // READ — get current field values

    
    public String getFirstName() {
        return WaitUtils.waitForVisible(driver, firstNameInput,
                WaitUtils.NAVIGATION_WAIT_SEC)
                .getAttribute("value").trim();
    }

    //current last name from the profile form. 
    public String getLastName() {
        return driver.findElement(lastNameInput).getAttribute("value").trim();
    }

    /** Returns the email shown on the profile (may be read-only). */
    public String getEmail() {
        return driver.findElement(emailInput).getAttribute("value").trim();
    }

    //phone value. 
    public String getPhone() {
        return driver.findElement(phoneInput).getAttribute("value").trim();
    }

    // ===========================================
   //Navigate Profile Page
    public void clickProfileBtn() {
        driver.findElement(profileBtn).click();
    }
    // Update field values
 
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

    // ====================================
    // CONVENIENCE — combined update operations

   
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

    // ========================================
    // ASSERTIONS SUPPORT — toast and validation errors
   
    
    public String getToastMessage() {
        try {
            return WaitUtils.waitForVisible(driver, toastBody,
                    WaitUtils.TOAST_WAIT_SEC).getText().trim();
        } catch (Exception e) {
            try {
                return WaitUtils.waitForVisible(driver, toastContainer,
                        WaitUtils.TOAST_WAIT_SEC).getText().trim();
            } catch (Exception ex) {
                return "";
            }
        }
    }
    public boolean isFirstNameErrorDisplayed() {
        return WaitUtils.isElementPresent(driver, firstNameError);
    }

   
    public boolean isLastNameErrorDisplayed() {
        return WaitUtils.isElementPresent(driver, lastNameError);
    }

    public String getFirstNameErrorText() {
        try {
            return WaitUtils.waitForVisible(driver, firstNameError,
                    WaitUtils.SHORT_WAIT_SEC).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
    public String getLastNameErrorText() {
        try {
            return WaitUtils.waitForVisible(driver, lastNameError,
                    WaitUtils.SHORT_WAIT_SEC).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
