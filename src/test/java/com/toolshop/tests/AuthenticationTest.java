package com.toolshop.tests;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.toolshop.constants.Constants;
import com.toolshop.pages.*;

public class AuthenticationTest extends TestBase {

	 
    // Positive Login Tests

    @Test(dataProvider = "loginDataPositive", description = "Valid credentials should log the user in")
    public void testSuccessfulLogin(String email, String password) {
        // Navigate to the login page via the navigation bar
        NavigationBar navBar = new NavigationBar(driver);
        driver.get(Constants.LOGIN_URL);

        // Perform login using the LoginPage POM
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);
        loginPage.clickLogin();

        // Assert: after a successful login the URL should no longer contain /auth/login
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/auth/login")));

        Assert.assertFalse(driver.getCurrentUrl().contains("/auth/login"),
                "Login failed — still on the login page for: " + email);
    }

    // Negative Login Tests — wrong password

    @Test(dataProvider = "loginDataNegativeWrongPassword",
          description = "Wrong password should show a password error message")
    public void testLoginWithWrongPassword(String email, String password) {
        driver.get(Constants.LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.iswrongPassMsgDisplayed(),
                "Expected password error message to be displayed for: " + email);
    }

    // Negative Login Tests — empty / invalid email

    @Test(dataProvider = "loginDataNegativeWrongEmail",
          description = "Invalid email should show an email validation error")
    public void testLoginWithInvalidEmail(String email, String password) {
        driver.get(Constants.LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.iswrongEmailMsgDisplayed(),
                "Expected email error message to be displayed for email: " + email);
    }

	
	
//Date Providers
	  @DataProvider(name = "loginDataPositive")
	    public Object[][] providePositiveLoginData1() {
	        return new Object[][] {
	                { "admin@practicesoftwaretesting.com",     "welcome01"  },
	                { "customer@practicesoftwaretesting.com",  "welcome01!" },
	                { "customer2@practicesoftwaretesting.com", "welcome01"  },
	                { "customer3@practicesoftwaretesting.com", "pass123"    }
	        };
	    }

	    @DataProvider(name = "loginDataNegativeWrongPassword")
	    public Object[][] provideNegativeLoginDataWrongPassword() {
	        return new Object[][] {
	                { "admin@practicesoftwaretesting.com",     "wrongpass"  },
	                { "customer@practicesoftwaretesting.com",  "123456"     },
	                { "customer2@practicesoftwaretesting.com", "badpassword"}
	        };
	    }

	    @DataProvider(name = "loginDataNegativeWrongEmail")
	    public Object[][] provideNegativeLoginDataWrongEmail() {
	        return new Object[][] {
	                { "notanemail",           "welcome01" },
	                { "",                     "welcome01" },
	                { "missing@domain",       "welcome01" }
	        };
	    }

}
