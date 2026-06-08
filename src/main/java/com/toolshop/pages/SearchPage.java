package com.toolshop.pages;

import com.toolshop.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage {

    private final WebDriver driver;

    // ─── Search Bar ───────────────────────────────────────────────────────────
    private final By searchBox = By.id("search-query");
    private final By searchBtn = By.cssSelector("[data-test='search-submit']");
    private final By searchResultsHeading = By.cssSelector("h3");
    // ─── Product Cards ────────────────────────────────────────────────────────
    private final By allCards         = By.cssSelector(".card");
    private final By cardProductName  = By.cssSelector("[data-test='product-name']");
    private final By cardProductPrice = By.cssSelector("[data-test='product-price']");
    private final By cardProductImage = By.tagName("img");

    // ─── Product Detail Page ──────────────────────────────────────────────────
    private final By detailProductName  = By.cssSelector("[data-test='product-name']");
    private final By detailUnitPrice    = By.cssSelector("[data-test='unit-price']");
    private final By detailProductImage = By.cssSelector(".img-fluid, app-product-detail img");
    private final By addToCartBtn       = By.cssSelector("[data-test='add-to-cart']");
    private final By addToFavoritesBtn  = By.cssSelector("[data-test='add-to-favorites']");

    // ─── Toast ────────────────────────────────────────────────────────────────
    // FIX #2: الـ toast في الموقع بيستخدم ngb-toast — نحط الـ selector الأصح أول
    private final By toastBody      = By.cssSelector("ngb-toast .toast-body, .toast-body");
    private final By toastContainer = By.cssSelector("ngb-toast, .toast, [role='alert']");

    // ─── Cart Page ────────────────────────────────────────────────────────────
    private final By cartProductTitle = By.cssSelector("[data-test='product-title']");
    private final By cartNavBadge     = By.cssSelector("[data-test='cart-quantity']");

    // ─── Favorites Page ───────────────────────────────────────────────────────
    private final By favProductName = By.cssSelector("[data-test='product-name']");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }
//============================================
    
// #Actions
    public void enterSearchKeyword(String keyword) {
        WaitUtils.waitForVisible(driver, searchBox).clear();
        driver.findElement(searchBox).sendKeys(keyword);
    }

    public void clickSearch() {
        WaitUtils.waitForClickable(driver, searchBtn).click();
    }

    public void searchFor(String keyword) {
        enterSearchKeyword(keyword);
        clickSearch();
    }

   
    public String getSearchResultsHeaderText() {
        return WaitUtils.waitForVisible(driver, searchResultsHeading,
                WaitUtils.NAVIGATION_WAIT_SEC).getText();
    }

    // ── Search Cards ──────────────────────────────────────────────────────────

    public WebElement getFirstResultCard() {
        WaitUtils.waitForVisible(driver, allCards, WaitUtils.NAVIGATION_WAIT_SEC);
        List<WebElement> cards = driver.findElements(allCards);
        if (cards.isEmpty()) {
            throw new RuntimeException("[SearchPage] No product cards found.");
        }
        return cards.get(0);
    }

    public String getFirstCardProductName() {
        return getFirstResultCard().findElement(cardProductName).getText().trim();
    }

    public String getFirstCardProductPrice() {
        return getFirstResultCard().findElement(cardProductPrice).getText().trim();
    }

    public boolean isFirstCardImageDisplayed() {
        try {
            WebElement img = getFirstResultCard().findElement(cardProductImage);
            String src = img.getAttribute("src");
            return img.isDisplayed() && src != null && !src.isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstCardImageSrc() {
        return getFirstResultCard().findElement(cardProductImage).getAttribute("src");
    }

    public void clickFirstResultCard() {
        WaitUtils.waitForVisible(driver, allCards, WaitUtils.NAVIGATION_WAIT_SEC);
        List<WebElement> cards = driver.findElements(allCards);
        if (cards.isEmpty()) {
            throw new RuntimeException("[SearchPage] No product cards to click.");
        }
        cards.get(0).click();
    }

    // Product Detail

    public String getDetailProductName() {
        return WaitUtils.waitForVisible(driver, detailProductName,
                WaitUtils.NAVIGATION_WAIT_SEC).getText().trim();
    }

    public String getDetailUnitPrice() {
        return WaitUtils.waitForVisible(driver, detailUnitPrice,
                WaitUtils.NAVIGATION_WAIT_SEC).getText().trim();
    }

    public boolean isDetailImageDisplayed() {
        try {
            WebElement img = WaitUtils.waitForVisible(driver, detailProductImage,
                    WaitUtils.NAVIGATION_WAIT_SEC);
            String src = img.getAttribute("src");
            return img.isDisplayed() && src != null && !src.isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDetailImageSrc() {
        return WaitUtils.waitForVisible(driver, detailProductImage,
                WaitUtils.NAVIGATION_WAIT_SEC).getAttribute("src");
    }

    public void clickAddToCart() {
        WaitUtils.waitForClickable(driver, addToCartBtn).click();
    }

    public boolean isAddToCartEnabled() {
        try {
            return WaitUtils.waitForVisible(driver, addToCartBtn,
                    WaitUtils.DEFAULT_WAIT_SEC).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddToFavorites() {
        WaitUtils.waitForClickable(driver, addToFavoritesBtn).click();
    }

    // Toast

    public String getToastMessage() {
        try {
            return WaitUtils.waitForVisible(driver, toastBody,
                    WaitUtils.TOAST_WAIT_SEC).getText().trim();
        } catch (Exception e) {
            try {
                return WaitUtils.waitForVisible(driver, toastContainer,
                        WaitUtils.TOAST_WAIT_SEC).getText().trim();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    // Cart 

    public int getCartBadgeCount() {
        try {
            String text = WaitUtils.waitForVisible(driver, cartNavBadge,
                    WaitUtils.SHORT_WAIT_SEC).getText().trim();
            return Integer.parseInt(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isProductInCart(String expectedProductName) {
        WaitUtils.waitForVisible(driver, cartProductTitle, WaitUtils.NAVIGATION_WAIT_SEC);
        List<WebElement> titles = driver.findElements(cartProductTitle);
        return titles.stream()
                .anyMatch(el -> el.getText().trim().equalsIgnoreCase(expectedProductName));
    }

    // ── Favorites ─────────────────────────────────────────────────────────────

    public boolean isProductInFavorites(String expectedProductName) {
        WaitUtils.waitForVisible(driver, favProductName, WaitUtils.NAVIGATION_WAIT_SEC);
        List<WebElement> names = driver.findElements(favProductName);
        return names.stream()
                .anyMatch(el -> el.getText().trim().equalsIgnoreCase(expectedProductName));
    }
}
