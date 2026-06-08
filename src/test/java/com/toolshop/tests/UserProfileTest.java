package com.toolshop.tests;

import com.aventstack.extentreports.ExtentTest;
import com.toolshop.constants.Constants;
import com.toolshop.dataProviders.ExcelDataProvider;
import com.toolshop.pages.LoginPage;
import com.toolshop.pages.UserProfilePage;
import com.toolshop.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserProfileTest extends TestBase {

    // ── Convenience shortcut ──────────────────────────────────────────────
    private ExtentTest log() {
        return extentTest.get();
    }

    // ── Login + navigate to profile ───────────────────────────────────────
    private UserProfilePage loginAndGoToProfile() {
        log().info("Logging in as: " + Constants.CUSTOMER_EMAIL);
        driver.get(Constants.LOGIN_URL);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(Constants.CUSTOMER_EMAIL, Constants.CUSTOMER_PASSWORD);
        WaitUtils.waitForUrlToNotContain(driver, "/auth/login");

        log().info("Navigating to profile page: " + Constants.ACCOUNT_URL);
        driver.get(Constants.ACCOUNT_URL);
        
        return new UserProfilePage(driver);
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

        // Assert 1 — first name not empty
        boolean firstNameNotEmpty = !firstName.isEmpty();
        if (firstNameNotEmpty) {
            log().pass("PASS — First name is pre-populated: \"" + firstName + "\"");
        } else {
            log().fail("FAIL — First name field is empty");
        }
        Assert.assertFalse(firstName.isEmpty(),
            "First name field is empty — expected a pre-populated value");

        // Assert 2 — email not empty
        boolean emailNotEmpty = !email.isEmpty();
        if (emailNotEmpty) {
            log().pass("PASS — Email field is not empty: \"" + email + "\"");
        } else {
            log().fail("FAIL — Email field is empty");
        }
        Assert.assertFalse(email.isEmpty(),
            "Email field is empty — expected the customer email to be shown");

        // Assert 3 — email contains @
        boolean emailValid = email.contains("@");
        if (emailValid) {
            log().pass("PASS — Email contains '@': \"" + email + "\"");
        } else {
            log().fail("FAIL — Email does not look like a valid address: \"" + email + "\"");
        }
        Assert.assertTrue(emailValid,
            "Email field value '" + email + "' does not look like a valid email address");
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
        profilePage.getToastMessage();   // wait for save to complete

        log().info("Reloading profile page to verify persistence");
        driver.get(Constants.ACCOUNT_URL);
        UserProfilePage reloadedProfile = new UserProfilePage(driver);

        String savedFirstName = reloadedProfile.getFirstName();
        String savedLastName  = reloadedProfile.getLastName();

        log().info("After reload — First Name: \"" + savedFirstName
                   + "\" | Last Name: \"" + savedLastName + "\"");

        // Assert 1 — first name persisted
        boolean firstNamePersisted = savedFirstName.equals(newFirstName);
        if (firstNamePersisted) {
            log().pass("PASS — First name persisted: \"" + savedFirstName + "\"");
        } else {
            log().fail("FAIL — First name did not persist. Expected: \""
                       + newFirstName + "\" | Actual: \"" + savedFirstName + "\"");
        }
        Assert.assertEquals(savedFirstName, newFirstName,
            "First name did not persist after save. Expected: '" + newFirstName + "'");

        // Assert 2 — last name persisted
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
