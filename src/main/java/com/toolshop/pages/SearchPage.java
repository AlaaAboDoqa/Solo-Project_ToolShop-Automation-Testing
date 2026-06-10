package com.toolshop.pages;

import com.toolshop.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class SearchPage {

    private final WebDriver driver;

    // ─── Search Bar ───────────────────────────────────────────────────────────
    private final By searchBox = By.id("search-query");
    private final By searchBtn = By.cssSelector("[data-test='search-submit']");
    private final By searchResultsHeading = By.cssSelector("[data-test=\"search-caption\"]");
    
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

    // ─── Actions ─────────────────────────────────────────────────────────────
    
    public void enterSearchKeyword(String keyword) {
        WebElement input = WaitUtils.waitForVisible(driver, searchBox);
        WaitUtils.waitForClickable(driver, searchBox);
        input.clear();
        input.sendKeys(keyword);
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

    // ─── Search Cards ──────────────────────────────────────────────────────────

    public WebElement getFirstResultCard() {
        WaitUtils.waitForVisible(driver, allCards, WaitUtils.NAVIGATION_WAIT_SEC);
        List<WebElement> cards = driver.findElements(allCards);
        if (cards.isEmpty()) {
            throw new RuntimeException("[SearchPage] No product cards found.");
        }
        return cards.get(0);
    }

    public String getFirstCardProductName() {
        WaitUtils.waitForVisible(driver, cardProductName, WaitUtils.NAVIGATION_WAIT_SEC);
        return getFirstResultCard().findElement(cardProductName).getText().trim();
    }

    public String getFirstCardProductPrice() {
        WaitUtils.waitForVisible(driver, cardProductPrice, WaitUtils.NAVIGATION_WAIT_SEC);
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

    // ─── Product Detail ───────────────────────────────────────────────────────

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

    // ─── Toast ────────────────────────────────────────────────────────────────

    public String getToastMessage() {
        try {
            WebElement toast = WaitUtils.waitForVisible(driver, toastBody, WaitUtils.TOAST_WAIT_SEC);
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d -> !toast.getText().trim().isEmpty());
            return toast.getText().trim();
        } catch (Exception e) {
            try {
                WebElement container = WaitUtils.waitForVisible(driver, toastContainer, WaitUtils.TOAST_WAIT_SEC);
                new WebDriverWait(driver, Duration.ofSeconds(3)).until(d -> !container.getText().trim().isEmpty());
                return container.getText().trim();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    // ─── Cart ─────────────────────────────────────────────────────────────────

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

    // ─── Favorites ─────────────────────────────────────────────────────────────

    public boolean isProductInFavorites(String expectedProductName) {
        WaitUtils.waitForVisible(driver, favProductName, WaitUtils.NAVIGATION_WAIT_SEC);
        List<WebElement> names = driver.findElements(favProductName);
        return names.stream()
                .anyMatch(el -> el.getText().trim().equalsIgnoreCase(expectedProductName));
    }
}