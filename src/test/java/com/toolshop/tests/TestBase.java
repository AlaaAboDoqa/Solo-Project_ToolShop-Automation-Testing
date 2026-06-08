package com.toolshop.tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.toolshop.constants.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;


public class TestBase {

    //WebDriver
    protected WebDriver driver;

    //Reporter
    protected static ExtentReports extent;

    protected static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // ── Report output path ─────────────────────────────────────────────────
    private static final String REPORT_PATH =
            System.getProperty("user.dir") + "/test-output/ExtentReport.html";

    //===================
    // Suite SetUp / TEARDOWN

    @BeforeSuite
    public void setUpReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("ToolShop Automation Report");
        spark.config().setReportName("ToolShop — QA Automation Suite");
        spark.config().setTimeStampFormat("dd MMM yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        // System info shown in the report header
        extent.setSystemInfo("Application", "ToolShop — practicesoftwaretesting.com");
        extent.setSystemInfo("Framework",   "Selenium 4 + TestNG + POM");
        extent.setSystemInfo("Environment", "Chrome — Windows");
        extent.setSystemInfo("Author",      "ToolShop QA Team");
    }

    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();   // writes all buffered logs to the HTML file
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // METHOD SETUP / TEARDOWN
    // ══════════════════════════════════════════════════════════════════════

    @BeforeMethod
    public void setUp(java.lang.reflect.Method method) {
        // ── Browser ───────────────────────────────────────────────────────
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(Constants.BASE_URL);

        ExtentTest test = extent.createTest(method.getName());
        extentTest.set(test);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                test.fail(result.getThrowable());
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.skip("Test was skipped: " + result.getThrowable());
            } else {
                test.pass("Test passed");
            }
        }

        // Exit browser 
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
