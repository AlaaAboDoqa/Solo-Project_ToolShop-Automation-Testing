package com.toolshop.tests;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.toolshop.pages.SearchPage;
public class TestBase {
	 public WebDriver driver;
	    public SearchPage searchPage;

	    @BeforeMethod
	    public void setUp() {
	    	ChromeOptions options = new ChromeOptions();

	        // 1. Hide the "Chrome is being controlled by automated software" banner
	        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

	        // 2. Disable the automation extension
	        options.addArguments("--disable-blink-features=AutomationControlled");

	        // Initialize driver with stealth options
	        driver = new ChromeDriver(options);
	        
	        driver.manage().window().maximize();
	        driver.get("https://practicesoftwaretesting.com/category/hand-tools");

	        // Initialize the Page Object
	        searchPage = new SearchPage(driver);
	    }
	    @AfterMethod
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
}}
