package com.toolshop.constants;

/**
 * Constants — Application-wide constant values.
 *
 * All URL strings, Excel file paths, and shared string literals live here.
 * Wait durations are in WaitUtils.java.
 */
public class Constants {

    // ─── Application URLs ─────────────────────────────────────────────────────
    public static final String BASE_URL                   = "https://practicesoftwaretesting.com";
    public static final String LOGIN_URL                  = BASE_URL + "/auth/login";
    public static final String ACCOUNT_URL                = BASE_URL + "/account";
   public static final String EDIT_PROFILE_URL                = ACCOUNT_URL + "/profile";

    public static final String FAVORITES_URL              = BASE_URL + "/account/favorites";
    public static final String CART_URL                   = BASE_URL + "/checkout";
    public static final String CATEGORY_HAND_TOOLS_URL    = BASE_URL + "/category/hand-tools";
    public static final String CATEGORY_POWER_TOOLS_URL   = BASE_URL + "/category/power-tools";

    // ─── Test Data File ───────────────────────────────────────────────────────
    public static final String EXCEL_DATA_FILE =
            System.getProperty("user.dir")
            + "/src/test/resources/testdata/ToolShop_TestData.xlsx";

    // ─── Excel Sheet Names ────────────────────────────────────────────────────
    public static final String SHEET_LOGIN_POSITIVE       = "LoginPositive";
    public static final String SHEET_LOGIN_NEG_PASSWORD   = "LoginNegPassword";
    public static final String SHEET_LOGIN_NEG_EMAIL      = "LoginNegEmail";
    public static final String SHEET_SEARCH_KEYWORDS      = "SearchKeywords";
    public static final String SHEET_CART_PRODUCTS        = "CartProducts";
    public static final String SHEET_PROFILE_UPDATES      = "ProfileUpdates";

    // ─── Shared Test Credentials (used for flows that require a login first) ──
    public static final String NEW_CUSTOMER_EMAIL             = "user_test_2026_xyz@gmail.com";
    public static final String NEW_CUSTOMER_PASSWORD          = "My-pass123";


    public static final String CUSTOMER_EMAIL             = "customer@practicesoftwaretesting.com";
    public static final String CUSTOMER_PASSWORD          = "welcome01";

    // ─── Expected Messages ────────────────────────────────────────────────────
    public static final String MSG_ADDED_TO_CART          = "Product added to shopping cart.";
    public static final String MSG_ADDED_TO_FAVORITES     = "Product added to your favorites list";
}
