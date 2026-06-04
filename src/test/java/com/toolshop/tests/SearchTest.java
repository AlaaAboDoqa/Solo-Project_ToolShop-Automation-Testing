package com.toolshop.tests;

	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.testng.Assert;
	import org.testng.annotations.AfterMethod;
	import org.testng.annotations.Test;

import com.toolshop.pages.SearchPage;

	public class SearchTest extends TestBase{
	   
	    @Test
	    public void verifyProductSearchExecution() {
	        String searchKeyword = "MacBook";
	        
	        // Execute steps using POM
	        searchPage.enterSearchKeyword(searchKeyword);
	        searchPage.clickSearch();
	        
	        // Assert the result using TestNG
	        String actualHeaderText = searchPage.getSearchHeaderText();
	        Assert.assertTrue(actualHeaderText.contains("Search - " + searchKeyword), 
	            "The search header text did not match the expected keyword!");
	    }

	    @AfterMethod
	    public void tearDown() {
	        if (driver != null) {
	            driver.quit();
	        }
	    }
}
