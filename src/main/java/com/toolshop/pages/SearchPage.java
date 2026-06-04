package com.toolshop.pages;


	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.support.ui.ExpectedConditions;
	import org.openqa.selenium.support.ui.WebDriverWait;
	import java.time.Duration;

	public class SearchPage {
	    private WebDriver driver;
	    private WebDriverWait wait;

	    // 1. Locators
	    private By searchBox = By.name("search");
	    private By searchButton = By.cssSelector("#search button");
	    private By searchHeader = By.cssSelector("#content h1");

	    // 2. Constructor
	    public SearchPage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    }

	    // 3. Actions
	    public void enterSearchKeyword(String keyword) {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).clear();
	        driver.findElement(searchBox).sendKeys(keyword);
	    }

	    public void clickSearch() {
	        driver.findElement(searchButton).click();
	    }

	    public String getSearchHeaderText() {
	        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchHeader)).getText();
	    }
	}

