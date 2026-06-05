package com.toolshop.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
private By passErrorMessage = By.xpath("//div[@id=\"password-error\"]");
private By emailErrorMessage= By.xpath("//div[@id=\"email-error\"]");
private By emailInput= By.id("email");
private By passwordInput= By.id("password");
private By LoginBtn= By.xpath("//input[@value='Login']");
private WebDriver driver;

//Constructor
public LoginPage(WebDriver driver) {
    this.driver = driver;
}
//Login Class to clear the inputs and enter the new values 
public void login(String email, String password) {
	driver.findElement(passwordInput).clear();
	driver.findElement(passwordInput).sendKeys(password);
	driver.findElement(emailInput).clear();
	driver.findElement(emailInput).sendKeys(email);
}
	
// Method to click the Login Button
	public void clickLogin() {

		driver.findElement(LoginBtn).click();

	}
	public boolean iswrongEmailMsgDisplayed() {
		if(driver.findElement(emailErrorMessage).isDisplayed())
		return true;
		else
			return false;
	}
	public boolean iswrongPassMsgDisplayed() {
		if(driver.findElement(passErrorMessage).isDisplayed())
		return true;
		else
			return false;
	}

}


