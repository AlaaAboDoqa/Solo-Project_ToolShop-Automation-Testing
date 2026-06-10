package com.toolshop.tests;
 
import com.aventstack.extentreports.ExtentTest;
import com.toolshop.constants.Constants;
import com.toolshop.dataProviders.ExcelDataProvider;
import com.toolshop.pages.LoginPage;
import com.toolshop.pages.UserProfilePage;
import com.toolshop.utils.WaitUtils;
 
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
 
public class UserProfileTest extends TestBase {
 
    // ── Convenience shortcut ──────────────────────────────────────────────
    private ExtentTest log() {
        return extentTest.get();
    }
 
    // ── Login + navigate to profile ───────────────────────────────────────
    private UserProfilePage loginAndGoToProfile() {
        log().info("Logging in as: " + Constants.NEW_CUSTOMER_EMAIL);
        driver.get(Constants.LOGIN_URL);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(Constants.NEW_CUSTOMER_EMAIL, Constants.NEW_CUSTOMER_PASSWORD);
        // Wait until login is complete (URL leaves /auth/login)
        WaitUtils.waitForUrlToNotContain(driver, "/auth/login");
 
        log().info("Navigating to profile page via nav link");
        // Click the profile nav link
        WaitUtils.waitForClickable(driver, By.xpath("//a[@data-test='nav-profile']")).click();
 
        UserProfilePage profilePage = new UserProfilePage(driver);
        //  Wait for the first-name field to be visible before returning.
        // Previously the method returned immediately after click(), before the
        // profile page had loaded — causing TimeoutException in every UP test.
        WaitUtils.waitForVisible(driver, By.cssSelector("[data-test='first-name']"),
                WaitUtils.NAVIGATION_WAIT_SEC);
 
        log().info("Profile page loaded: " + Constants.ACCOUNT_URL);
        return profilePage;
    }
 
    // ══════════════════════════════════════════════════════════════════════
    // UP-001 — Profile page displays current user details
    // ══════════════════════════════════════════════════════════════════════
 
    @Test(description = "UP-001: Profile page should display pre-populated first name and email")
    public void verifyProfilePageDisplaysCurrentDetails() {
        log().assignCategory("User Profile");
 
        UserProfilePage profilePage = loginAndGoToProfile();
 
        String firstName = profilePage.getFirstName();
        String email     = profilePage.getEmail();
 
        log().info("First Name field value: \"" + firstName + "\"");
        log().info("Email field value:      \"" + email + "\"");
 
        org.testng.asserts.SoftAssert softAssert = new org.testng.asserts.SoftAssert();
 
        softAssert.assertNotNull(firstName, "First name value is null!");
        if (firstName != null) {
            softAssert.assertFalse(firstName.isEmpty(),
                "First name field is empty — expected a pre-populated value");
        }
 
        softAssert.assertNotNull(email, "Email value is null!");
        if (email != null) {
            softAssert.assertFalse(email.isEmpty(),
                "Email field is empty — expected the customer email to be shown");
            softAssert.assertTrue(email.contains("@"),
                "Email field value '" + email + "' does not contain '@'");
        }
 
        softAssert.assertAll();
    }
 
    // ══════════════════════════════════════════════════════════════════════
    // UP-002 — Valid profile update shows success toast
    // ══════════════════════════════════════════════════════════════════════
 
    @Test(
        dataProvider      = "profileUpdates",
        dataProviderClass = ExcelDataProvider.class,
        description       = "UP-002: Valid profile update should show a success toast"
    )
    public void verifyProfileUpdateWithValidData(String newFirstName, String newLastName) {
        log().assignCategory("User Profile");
 
        UserProfilePage profilePage = loginAndGoToProfile();
 
        log().info("Updating profile — First Name: \"" + newFirstName
                   + "\" | Last Name: \"" + newLastName + "\"");
        profilePage.updateFullName(newFirstName, newLastName);
 
        // FIX 4: getToastMessage() is called once here; the fix is inside
        // UserProfilePage — the fallback selectors now share one wait call
        // so the toast is not missed on fast responses.
        String toast = profilePage.getToastMessage();
        log().info("Toast message received: \"" + toast + "\"");
 
        boolean toastCorrect = toast.toLowerCase().contains("updated")
                            || toast.toLowerCase().contains("profile");
        if (toastCorrect) {
            log().pass("PASS — Profile update success toast displayed: \"" + toast + "\"");
        } else {
            log().fail("FAIL — Expected profile update toast but got: \"" + toast + "\"");
        }
 
        Assert.assertTrue(toastCorrect,
            "Expected a 'profile updated' success message. Actual toast: '" + toast + "'");
    }
 
    // ══════════════════════════════════════════════════════════════════════
    // UP-003 — Updated name persists after page reload
    // ══════════════════════════════════════════════════════════════════════
 
    @Test(
        dataProvider      = "profileUpdates",
        dataProviderClass = ExcelDataProvider.class,
        description       = "UP-003: Updated profile values should persist after page reload"
    )
    public void verifyUpdatedNamePersistsAfterSave(String newFirstName, String newLastName) {
        log().assignCategory("User Profile");
 
        UserProfilePage profilePage = loginAndGoToProfile();
 
        log().info("Updating profile — First Name: \"" + newFirstName
                   + "\" | Last Name: \"" + newLastName + "\"");
        profilePage.updateFullName(newFirstName, newLastName);
        profilePage.getToastMessage(); // wait for save to complete
 
        log().info("Reloading profile page to verify persistence");
        driver.get(Constants.ACCOUNT_URL);
        UserProfilePage reloadedProfile = new UserProfilePage(driver);
        // FIX 1 (same as loginAndGoToProfile): wait for the page to fully load
        // before reading field values, otherwise getFirstName/getLastName time out.
        WaitUtils.waitForVisible(driver, By.cssSelector("[data-test='first-name']"),
                WaitUtils.NAVIGATION_WAIT_SEC);
 
        String savedFirstName = reloadedProfile.getFirstName();
        String savedLastName  = reloadedProfile.getLastName();
 
        log().info("After reload — First Name: \"" + savedFirstName
                   + "\" | Last Name: \"" + savedLastName + "\"");
 
        boolean firstNamePersisted = savedFirstName.equals(newFirstName);
        if (firstNamePersisted) {
            log().pass("PASS — First name persisted: \"" + savedFirstName + "\"");
        } else {
            log().fail("FAIL — First name did not persist. Expected: \""
                       + newFirstName + "\" | Actual: \"" + savedFirstName + "\"");
        }
        Assert.assertEquals(savedFirstName, newFirstName,
            "First name did not persist after save. Expected: '" + newFirstName + "'");
 
        boolean lastNamePersisted = savedLastName.equals(newLastName);
        if (lastNamePersisted) {
            log().pass("PASS — Last name persisted: \"" + savedLastName + "\"");
        } else {
            log().fail("FAIL — Last name did not persist. Expected: \""
                       + newLastName + "\" | Actual: \"" + savedLastName + "\"");
        }
        Assert.assertEquals(savedLastName, newLastName,
            "Last name did not persist after save. Expected: '" + newLastName + "'");
    }
 
    // ══════════════════════════════════════════════════════════════════════
    // UP-004 — Blank first name shows validation error
    // ══════════════════════════════════════════════════════════════════════
 
    @Test(description = "UP-004: Blank first name should display a required-field validation error")
    public void verifyBlankFirstNameShowsValidationError() {
        log().assignCategory("User Profile");
 
        UserProfilePage profilePage = loginAndGoToProfile();
 
        log().info("Clearing first name field and submitting the form");
        profilePage.submitWithBlankFirstName();
 
        // FIX 2: use isFirstNameErrorDisplayed() which now uses waitForVisible
        // instead of isElementPresent (which doesn't wait for the DOM update)
        boolean errorDisplayed = profilePage.isFirstNameErrorDisplayed();
        log().info("First name validation error displayed: " + errorDisplayed);
 
        if (errorDisplayed) {
            log().pass("PASS — Validation error shown for blank first name");
        } else {
            log().fail("FAIL — No validation error shown after submitting blank first name");
        }
 
        Assert.assertTrue(errorDisplayed,
            "Expected a validation error on First Name after submitting blank value");
    }
 
    // ══════════════════════════════════════════════════════════════════════
    // UP-005 — Blank last name shows validation error
    // ══════════════════════════════════════════════════════════════════════
 
    @Test(description = "UP-005: Blank last name should display a required-field validation error")
    public void verifyBlankLastNameShowsValidationError() {
        log().assignCategory("User Profile");
 
        UserProfilePage profilePage = loginAndGoToProfile();
 
        log().info("Clearing last name field and submitting the form");
        profilePage.submitWithBlankLastName();
 
        // FIX 2: same as UP-004 — wait for validation error to appear
        boolean errorDisplayed = profilePage.isLastNameErrorDisplayed();
        log().info("Last name validation error displayed: " + errorDisplayed);
 
        if (errorDisplayed) {
            log().pass("PASS — Validation error shown for blank last name");
        } else {
            log().fail("FAIL — No validation error shown after submitting blank last name");
        }
 
        Assert.assertTrue(errorDisplayed,
            "Expected a validation error on Last Name after submitting blank value");
    }
}