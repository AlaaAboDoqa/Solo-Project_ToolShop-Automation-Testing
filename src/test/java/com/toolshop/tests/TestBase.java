package com.toolshop.tests;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import com.toolshop.constants.Constants;

import io.github.bonigarcia.wdm.WebDriverManager;
public class TestBase {

	protected WebDriver driver;	
	
	@BeforeTest
	public void setup() {
		
		WebDriverManager.firefoxdriver().setup();
		
		
		driver=new FirefoxDriver();
		
	    driver.get(Constants.BASE_URL) ;
		    }
	@AfterTest
	public void tearDown() throws InterruptedException {
		
		if (driver!= null) {
		  driver.quit();	
			
		}
		
	}
	
}
