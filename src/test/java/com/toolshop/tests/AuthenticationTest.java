package com.toolshop.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.toolshop.constants.Constants;
import com.toolshop.dataProviders.ExcelDataProvider;
import com.toolshop.pages.LoginPage;
import com.toolshop.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.Test;


public class AuthenticationTest extends TestBase {

    // Reporter
    private ExtentTest log() {
        return extentTest.get();
    }

    // ══════════════════════════════════════════════════════════════════════
    // AT-001 to AT-004 — Successful login with valid credentials
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "loginDataPositive",
        dataProviderClass = ExcelDataProvider.class,
        description       = "Valid credentials should authenticate and redirect the user"
    )
    public void testSuccessfulLogin(String email, String password) {
        log().assignCategory("Authentication")
             .info("Navigating to login page: " + Constants.LOGIN_URL);

        driver.get(Constants.LOGIN_URL);
        LoginPage loginPage = new LoginPage(driver);

        log().info("Entering credentials — Email: " + email);
        loginPage.loginAs(email, password);

        log().info("Waiting for URL to change away from /auth/login");
        WaitUtils.waitForUrlToNotContain(driver, "/auth/login");

        String currentUrl = driver.getCurrentUrl();
        log().info("Current URL after login attempt: " + currentUrl);

        boolean redirected = !currentUrl.contains("/auth/login");
        if (redirected) {
            log().pass("PASS — User redirected successfully. URL: " + currentUrl);
        } else {
            log().fail("FAIL — Still on /auth/login page for: " + email);
        }

        Assert.assertFalse(
            currentUrl.contains("/auth/login"),
            "Login failed — still on /auth/login for: " + email
        );
    }

    // ══════════════════════════════════════════════════════════════════════
    // AT-005, AT-008, AT-009 — Wrong password keeps user on login page
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "loginDataNegativeWrongPassword",
        dataProviderClass = ExcelDataProvider.class,
        description       = "Wrong password should keep the user on the login page"
    )
    public void testLoginWithWrongPassword(String email, String password) {
        log().assignCategory("Authentication")
             .info("Navigating to login page");

        driver.get(Constants.LOGIN_URL);
        LoginPage loginPage = new LoginPage(driver);

        log().info("Entering credentials with wrong password — Email: " + email
                   + " | Password: " + password);
        loginPage.loginAs(email, password);

        String currentUrl = driver.getCurrentUrl();
        log().info("Current URL after failed login: " + currentUrl);

        boolean stayedOnLogin = currentUrl.contains("/auth/login");
        if (stayedOnLogin) {
            log().pass("PASS — User remained on /auth/login as expected");
        } else {
            log().fail("FAIL — User was redirected away from login page unexpectedly. URL: "
                       + currentUrl);
        }

        Assert.assertTrue(
            stayedOnLogin,
            "Expected to remain on login page for wrong password. URL: " + currentUrl
        );
    }

    // ══════════════════════════════════════════════════════════════════════
    // AT-006, AT-007, AT-010, AT-011 — Invalid email format
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "loginDataNegativeWrongEmail",
        dataProviderClass = ExcelDataProvider.class,
        description       = "Invalid email format should prevent login"
    )
    public void testLoginWithInvalidEmail(String email, String password) {
        log().assignCategory("Authentication")
             .info("Navigating to login page");

        driver.get(Constants.LOGIN_URL);
        LoginPage loginPage = new LoginPage(driver);

        log().info("Entering invalid email: \"" + email + "\"");
        loginPage.loginAs(email, password);

        boolean stayedOnLogin = driver.getCurrentUrl().contains("/auth/login");
        boolean emailError    = loginPage.isEmailErrorDisplayed();

        log().info("Stayed on login page: " + stayedOnLogin
                   + " | Email error displayed: " + emailError);

        if (stayedOnLogin || emailError) {
            log().pass("PASS — Login blocked for invalid email: \"" + email + "\"");
        } else {
            log().fail("FAIL — Login was NOT blocked for invalid email: \"" + email + "\"");
        }

        Assert.assertTrue(
            stayedOnLogin || emailError,
            "Expected to stay on login page or show email error for: \"" + email + "\""
        );
    }
}
