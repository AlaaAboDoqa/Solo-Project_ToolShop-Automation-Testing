package com.toolshop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationBar {
// 1. Locators

    private By HomePageBtn = By.xpath("//a[text()='Home']");
    private By CategoriesBtn = By.cssSelector("#navbarSupportedContent > ul > li:nth-child(2) > button");
    private By SignInBtn = By.cssSelector("#navbarSupportedContent > ul > li:nth-child(4) > a");
    private WebDriver driver;
 //Constructor
    public NavigationBar(WebDriver driver) {
        this.driver = driver;
    }
    
//This Method is for the Categories Link in the NavBar
    public void clickCategoriesLink() {
    	
		driver.findElement(CategoriesBtn).click();

    }

//This Method is for the Categories Link in the NavBar

    public void clickLoginLink() {
	driver.findElement(SignInBtn).click();

}
    public void clickHomePageLink() {
    	
    	driver.findElement(HomePageBtn).click();
    }
    
    }
