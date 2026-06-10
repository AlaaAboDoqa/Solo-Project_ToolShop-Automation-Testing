# Tool Shop - Automation Testing Framework 
![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Tools-Selenium-green?style=for-the-badge&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/Framework-TestNG-8A2BE2?style=for-the-badge&logo=testng&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-red?style=for-the-badge&logo=apachemaven&logoColor=white)
![Status](https://img.shields.io/badge/Project%20Status-Passing-brightgreen?style=for-the-badge)

This repository contains the automated test suite for the **Tool Shop** e-commerce web application. The framework is designed using industry best practices to ensure high test coverage, reliability, and ease of maintenance.

---

##  Project Overview

The primary objective of this project is to automate the functional and regression testing of the Tool Shop platform. It ensures that critical user journeys—such as user authentication, product searching, and shopping cart management—work flawlessly across updates.

### Key Modules Automated:
* **Authentication:** Positive and negative login scenarios (Wrong passwords, invalid emails).
* **Search Functionality:** Validating search bar responsiveness and product filtering accuracy.
* **Cart & Checkout:** Verification of adding products to the shopping cart and persistent state validation.
* **User Profile:** Automation of profile information updates.

---

##  Architecture & Tech Stack

The framework is built using a robust, decoupled architecture to separate test logic from page layouts and data.

* **Language:** Java 8 / OpenJDK 21
* **Automation Tool:** Selenium WebDriver (Browser Automation)
* **Test Management:** TestNG (Assertions, Data-Driven Testing, Test Suites)
* **Build Tool:** Maven (Dependency & Lifecycle Management)
* **Design Pattern:** Page Object Model (POM) for clean locator management.
* **Data-Driven Testing:** Apache POI (Reading dynamic test cases from Excel).

---

##  Project Structure

```text
toolshop-automation-test/
├── src/
│   ├── main/java/
│   │   └── com/toolshop/
│   │       ├── dataProviders/       # TestNG Data Providers
│   │       ├── pages/               # Page Object Classes (Locators & Actions)
│   │       └── utils/               # ExcelReader, WaitUtils, and Constants
│   └── test/java/
│       └── com/toolshop/tests/      # TestNG Test Classes (AuthenticationTest, SearchTest, etc.)
│   └── test/resources/
│       ├── testdata/                # ToolShop_TestData.xlsx (Excel Data Sheets)
│       └── testng.xml               # Test Suite Configuration
├── pom.xml                          # Maven Configuration & Dependencies
└── README.md                        # Project Documentation
