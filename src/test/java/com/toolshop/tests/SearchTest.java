package com.toolshop.tests;

import com.aventstack.extentreports.ExtentTest;
import com.toolshop.constants.Constants;
import com.toolshop.dataProviders.ExcelDataProvider;
import com.toolshop.pages.LoginPage;
import com.toolshop.pages.NavigationBar;
import com.toolshop.pages.SearchPage;
import com.toolshop.utils.WaitUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * SearchTest — Search, Navigation, Product Detail, Cart, Favorites (ST-001 to ST-013).
 *
 * Every assertion is preceded by an ExtentReports log line so the HTML
 * report shows exactly which step passed or failed.
 */
public class SearchTest extends TestBase {

    // ── Convenience shortcut ──────────────────────────────────────────────
    private ExtentTest log() {
        return extentTest.get();
    }

    //  Login 
    private void loginAsCustomer() {
        log().info("Logging in as customer: " + Constants.NEW_CUSTOMER_EMAIL);
        driver.get(Constants.LOGIN_URL);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(Constants.NEW_CUSTOMER_EMAIL, Constants.NEW_CUSTOMER_PASSWORD);
        WaitUtils.waitForUrlToNotContain(driver, "/auth/login");
        log().info("Login successful — URL: " + driver.getCurrentUrl());
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-001 to ST-003 — Keyword search results heading
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "searchKeywords",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-001–003: Keyword search should show results heading containing the keyword"
    )
    public void verifyProductSearchExecution(String keyword) {
        log().assignCategory("Search");
        log().info("Searching for keyword: \"" + keyword + "\"");

        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor(keyword);

        String headerText = searchPage.getSearchResultsHeaderText();
        log().info("Search results heading text: \"" + headerText + "\"");

        boolean headingContainsKeyword = headerText.toLowerCase()
                                                    .contains(keyword.toLowerCase());
        if (headingContainsKeyword) {
            log().pass("PASS — Heading contains keyword \"" + keyword + "\"");
        } else {
            log().fail("FAIL — Heading \"" + headerText
                       + "\" does not contain keyword \"" + keyword + "\"");
        }

        Assert.assertTrue(
            headingContainsKeyword,
            "Results heading did not contain keyword '" + keyword
            + "'. Actual: " + headerText
        );
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-004 — Hand Tools category navigation
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "ST-004: Hand Tools category navigation")
    public void verifyHandToolsCategoryNavigation() {
        log().assignCategory("Navigation");
        log().info("Clicking Categories > Hand Tools");

        NavigationBar navBar = new NavigationBar(driver);
        navBar.navigateToHandTools();
        WaitUtils.waitForUrlToContain(driver, "hand-tools");

        String url = driver.getCurrentUrl();
        log().info("Current URL: " + url);

        boolean urlCorrect = url.contains("hand-tools");
        if (urlCorrect) {
            log().pass("PASS — URL contains 'hand-tools': " + url);
        } else {
            log().fail("FAIL — URL does not contain 'hand-tools': " + url);
        }

        Assert.assertTrue(urlCorrect,
            "URL did not contain 'hand-tools'. Actual: " + url);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-005 — Power Tools category navigation
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "ST-005: Power Tools category navigation")
    public void verifyPowerToolsCategoryNavigation() {
        log().assignCategory("Navigation");
        log().info("Clicking Categories > Power Tools");

        NavigationBar navBar = new NavigationBar(driver);
        navBar.navigateToPowerTools();
        WaitUtils.waitForUrlToContain(driver, "power-tools");

        String url = driver.getCurrentUrl();
        log().info("Current URL: " + url);

        boolean urlCorrect = url.contains("power-tools");
        if (urlCorrect) {
            log().pass("PASS — URL contains 'power-tools': " + url);
        } else {
            log().fail("FAIL — URL does not contain 'power-tools': " + url);
        }

        Assert.assertTrue(urlCorrect,
            "URL did not contain 'power-tools'. Actual: " + url);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-006 — Home button returns to home page
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "ST-006: Home button returns user to home page")
    public void verifyHomeNavigationFromCategoryPage() {
        log().assignCategory("Navigation");
        log().info("Navigating to: " + Constants.CATEGORY_HAND_TOOLS_URL);

        driver.get(Constants.CATEGORY_HAND_TOOLS_URL);
        NavigationBar navBar = new NavigationBar(driver);

        log().info("Clicking Home nav link");
        navBar.clickHomePageLink();
        WaitUtils.waitForUrlToContain(driver, Constants.BASE_URL);

        String url = driver.getCurrentUrl();
        log().info("Current URL after clicking Home: " + url);

        boolean onHome = url.equals(Constants.BASE_URL + "/") || url.equals(Constants.BASE_URL);
        if (onHome) {
            log().pass("PASS — Returned to home page: " + url);
        } else {
            log().fail("FAIL — Not on home page. Actual URL: " + url);
        }

        Assert.assertTrue(onHome, "Expected home URL. Actual: " + url);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-007 — Search card shows product name and price
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "searchKeywords",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-007: First search result card should display a name and price"
    )
    public void verifySearchResultCardNameAndPrice(String keyword) {
        log().assignCategory("Product Card");
        log().info("Searching for keyword: \"" + keyword + "\"");

        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor(keyword);

        String cardName  = searchPage.getFirstCardProductName();
        String cardPrice = searchPage.getFirstCardProductPrice();

        log().info("Card product name:  \"" + cardName + "\"");
        log().info("Card product price: \"" + cardPrice + "\"");

        // Assert 1 — name not empty
        boolean nameNotEmpty = !cardName.isEmpty();
        if (nameNotEmpty) {
            log().pass("PASS — Product name is not empty: \"" + cardName + "\"");
        } else {
            log().fail("FAIL — Product name is empty for keyword: \"" + keyword + "\"");
        }
        Assert.assertFalse(cardName.isEmpty(),
            "Product name on search card is empty for keyword: " + keyword);

        // Assert 2 — price starts with $
        boolean priceValid = cardPrice.startsWith("$") && cardPrice.length() > 1;
        if (priceValid) {
            log().pass("PASS — Product price has valid format: \"" + cardPrice + "\"");
        } else {
            log().fail("FAIL — Product price has unexpected format: \"" + cardPrice + "\"");
        }
        Assert.assertTrue(priceValid,
            "Product price on search card has unexpected format '" + cardPrice
            + "' for keyword: " + keyword);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-008 — Search card shows product image
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "searchKeywords",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-008: First search result card should display a product image"
    )
    public void verifySearchResultCardImageDisplayed(String keyword) {
        log().assignCategory("Product Card");
        log().info("Searching for keyword: \"" + keyword + "\"");

        SearchPage searchPage = new SearchPage(driver); 
        searchPage.searchFor(keyword);

        boolean imageDisplayed = searchPage.isFirstCardImageDisplayed();
        log().info("Product image displayed on card: " + imageDisplayed);

        if (imageDisplayed) {
            log().pass("PASS — Product image is visible on the search card");
        } else {
            log().fail("FAIL — Product image is NOT visible on the search card for keyword: \""
                       + keyword + "\"");
        }

        Assert.assertTrue(imageDisplayed,
            "Product image on search card is not displayed for keyword: " + keyword);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-009 — Product detail page matches search card
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "searchKeywords",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-009: Product detail page name, price and image must match the search card"
    )
    public void verifyProductDetailMatchesSearchCard(String keyword) {
        log().assignCategory("Product Detail");
        log().info("Searching for keyword: \"" + keyword + "\"");

        SearchPage searchPage = new SearchPage(driver);
        searchPage.searchFor(keyword);

        // Read card data
        String cardName  = searchPage.getFirstCardProductName();
        String cardPrice = searchPage.getFirstCardProductPrice();
        log().info("Search card — Name: \"" + cardName + "\" | Price: \"" + cardPrice + "\"");

        // Navigate to detail page
        log().info("Clicking first search result card");
        searchPage.clickFirstResultCard();

        // Read detail data
        String detailName  = searchPage.getDetailProductName();
        String detailPrice = "$"+searchPage.getDetailUnitPrice();
        boolean imgVisible = searchPage.isDetailImageDisplayed();
        log().info("Detail page — Name: \"" + detailName
                   + "\" | Price: \"" + detailPrice
                   + "\" | Image visible: " + imgVisible);

        // Assert 1 — name matches
        boolean nameMatch = detailName.equals(cardName);
        if (nameMatch) {
            log().pass("PASS — Detail name matches card name: \"" + detailName + "\"");
        } else {
            log().fail("FAIL — Name mismatch. Card: \"" + cardName
                       + "\" | Detail: \"" + detailName + "\"");
        }
        Assert.assertEquals(detailName, cardName,
            "Detail name '" + detailName + "' does not match card name '" + cardName + "'");

        // Assert 2 — price matches
        boolean priceMatch = detailPrice.equals(cardPrice);
        if (priceMatch) {
            log().pass("PASS — Detail price matches card price: \"" + detailPrice + "\"");
        } else {
            log().fail("FAIL — Price mismatch. Card: \"" + cardPrice
                       + "\" | Detail: \"" + detailPrice + "\"");
        }
        Assert.assertEquals(detailPrice, cardPrice,
            "Detail price '" + detailPrice + "' does not match card price '" + cardPrice + "'");

        // Assert 3 — image visible
        if (imgVisible) {
            log().pass("PASS — Product image is visible on the detail page");
        } else {
            log().fail("FAIL — Product image is NOT visible on the detail page for: \""
                       + detailName + "\"");
        }
        Assert.assertTrue(imgVisible,
            "Product image is not displayed on the detail page for: " + detailName);
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-010 — Add to Cart: success toast is shown
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "cartProducts",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-010: Add to Cart should display a success toast message"
    )
    public void verifyAddToCartSuccessMessage(String searchKeyword, String expectedProductName) {
        log().assignCategory("Cart");
        loginAsCustomer();

        driver.get(Constants.BASE_URL);
        SearchPage searchPage = new SearchPage(driver);

        log().info("Searching for: \"" + searchKeyword + "\"");
        searchPage.searchFor(searchKeyword);
        searchPage.clickFirstResultCard();

        log().info("Clicking Add to Cart button");
        searchPage.clickAddToCart();

        String toast = searchPage.getToastMessage();
        log().info("Toast message received: \"" + toast + "\"");

        boolean toastCorrect = toast.contains("added") || toast.contains("cart");
        if (toastCorrect) {
            log().pass("PASS — Cart success toast displayed: \"" + toast + "\"");
        } else {
            log().fail("FAIL — Expected cart toast but got: \"" + toast + "\"");
        }

        Assert.assertTrue(toastCorrect,
            "Expected cart success toast. Actual: '" + toast + "'");
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-011 — Add to Cart: product appears in cart page
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "cartProducts",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-011: Product should appear in the cart page after Add to Cart"
    )
    public void verifyProductAppearsInCartAfterAddToCart(String searchKeyword,
                                                         String expectedProductName) {
        log().assignCategory("Cart");
        loginAsCustomer();

        driver.get(Constants.BASE_URL);
        SearchPage searchPage = new SearchPage(driver);

        log().info("Searching for: \"" + searchKeyword + "\"");
        searchPage.searchFor(searchKeyword);

        String actualProductName = searchPage.getFirstCardProductName();
        log().info("Product name from card: \"" + actualProductName + "\"");

        searchPage.clickFirstResultCard();

        log().info("Clicking Add to Cart");
        searchPage.clickAddToCart();

        log().info("Navigating to cart page: " + Constants.CART_URL);
        driver.get(Constants.CART_URL);

        boolean inCart = searchPage.isProductInCart(actualProductName);
        log().info("Product found in cart: " + inCart);

        if (inCart) {
            log().pass("PASS — \"" + actualProductName + "\" is present in the cart");
        } else {
            log().fail("FAIL — \"" + actualProductName + "\" was NOT found in the cart");
        }

        Assert.assertTrue(inCart,
            "Product '" + actualProductName + "' not found in cart. "
            + "Searched via keyword: '" + searchKeyword + "'");
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-012 — Add to Favorites: success message is shown
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "cartProducts",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-012: Add to Favorites should display a success message"
    )
    public void verifyAddToFavoritesSuccessMessage(String searchKeyword,
                                                   String expectedProductName) {
        log().assignCategory("Favorites");
        loginAsCustomer();

        driver.get(Constants.BASE_URL);
        SearchPage searchPage = new SearchPage(driver);

        log().info("Searching for: \"" + searchKeyword + "\"");
        searchPage.searchFor(searchKeyword);
        searchPage.clickFirstResultCard();

        log().info("Clicking Add to Favorites button");
        searchPage.clickAddToFavorites();

        String toast = searchPage.getToastMessage();
        log().info("Toast message received: \"" + toast + "\"");

        boolean toastCorrect = toast.toLowerCase().contains("favorite")
                            || toast.toLowerCase().contains("wishlist");
        if (toastCorrect) {
            log().pass("PASS — Favorites success message displayed: \"" + toast + "\"");
        } else {
            log().fail("FAIL — Expected favorites toast but got: \"" + toast + "\"");
        }

        Assert.assertTrue(toastCorrect,
            "Expected favorites success message. Actual: '" + toast + "'");
    }

    // ══════════════════════════════════════════════════════════════════════
    // ST-013 — Add to Favorites: product appears on favorites page
    // ══════════════════════════════════════════════════════════════════════

    @Test(
        dataProvider      = "cartProducts",
        dataProviderClass = ExcelDataProvider.class,
        description       = "ST-013: Product should appear on the favorites page after Add to Favorites"
    )
    public void verifyProductAppearsInFavoritesAfterAdd(String searchKeyword,
                                                        String expectedProductName) {
        log().assignCategory("Favorites");
        loginAsCustomer();

        driver.get(Constants.BASE_URL);
        SearchPage searchPage = new SearchPage(driver);

        log().info("Searching for: \"" + searchKeyword + "\"");
        searchPage.searchFor(searchKeyword);

        String actualProductName = searchPage.getFirstCardProductName();
        log().info("Product name from card: \"" + actualProductName + "\"");

        searchPage.clickFirstResultCard();

        log().info("Clicking Add to Favorites");
        searchPage.clickAddToFavorites();

        log().info("Navigating to favorites page: " + Constants.FAVORITES_URL);
        driver.get(Constants.FAVORITES_URL);

        boolean inFavorites = searchPage.isProductInFavorites(actualProductName);
        log().info("Product found on favorites page: " + inFavorites);

        if (inFavorites) {
            log().pass("PASS — \"" + actualProductName + "\" is present on the favorites page");
        } else {
            log().fail("FAIL — \"" + actualProductName + "\" was NOT found on the favorites page");
        }

        Assert.assertTrue(inFavorites,
            "Product '" + actualProductName + "' not found on favorites page. "
            + "Searched via keyword: '" + searchKeyword + "'");
    }
}
