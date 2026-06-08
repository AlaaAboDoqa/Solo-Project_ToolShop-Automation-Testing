package com.toolshop.dataProviders;
import com.toolshop.constants.Constants;
import com.toolshop.utils.ExcelReader;
import org.testng.annotations.DataProvider;

/**
 * ExcelDataProvider — All TestNG @DataProvider methods for the ToolShop suite.
 *
 * Every method reads its data from the external Excel workbook defined in
 * Constants.EXCEL_DATA_FILE using ExcelReader. Tests reference these providers
 * with:
 *
 *   @Test(dataProvider = "providerName", dataProviderClass = ExcelDataProvider.class)
 *
 * ─── Full Excel Workbook Layout ──────────────────────────────────────────────
 *
 *  Sheet: "LoginPositive"          Cols: email | password
 *  Sheet: "LoginNegPassword"       Cols: email | password
 *  Sheet: "LoginNegEmail"          Cols: email | password
 *  Sheet: "SearchKeywords"         Cols: keyword
 *  Sheet: "CartProducts"           Cols: searchKeyword | expectedProductName
 *  Sheet: "ProfileUpdates"         Cols: newFirstName | newLastName
 *
 *  Row 0 in every sheet = header row (always skipped by ExcelReader).
 *
 * ─── CartProducts sheet layout ───────────────────────────────────────────────
 *
 *  Col A: searchKeyword       — keyword to search for (e.g. "Hammer")
 *  Col B: expectedProductName — exact product name to verify in cart / favorites
 *
 *  Example rows:
 *    searchKeyword    | expectedProductName
 *    Hammer           | Thor Hammer
 *    Pliers           | Slip Joint Pliers
 *
 * ─── ProfileUpdates sheet layout ─────────────────────────────────────────────
 *
 *  Col A: newFirstName  — new first name to update (e.g. "Alaa")
 *  Col B: newLastName   — new last name to update (e.g. "Tester")
 *
 *  Example rows:
 *    newFirstName | newLastName
 *    Alaa         | Tester
 *    Jane         | QA
 */
public class ExcelDataProvider {

    private static final ExcelReader reader = new ExcelReader(Constants.EXCEL_DATA_FILE);

    // ─── Authentication DataProviders ─────────────────────────────────────────

    @DataProvider(name = "loginDataPositive")
    public Object[][] providePositiveLoginData() {
        return reader.readSheet(Constants.SHEET_LOGIN_POSITIVE, 2);
    }

    @DataProvider(name = "loginDataNegativeWrongPassword")
    public Object[][] provideNegativeLoginDataWrongPassword() {
        return reader.readSheet(Constants.SHEET_LOGIN_NEG_PASSWORD, 2);
    }

    @DataProvider(name = "loginDataNegativeWrongEmail")
    public Object[][] provideNegativeLoginDataWrongEmail() {
        return reader.readSheet(Constants.SHEET_LOGIN_NEG_EMAIL, 2);
    }

    // ─── Search DataProviders ─────────────────────────────────────────────────

    /** Single column: [keyword] — used by keyword-search tests. */
    @DataProvider(name = "searchKeywords")
    public Object[][] provideSearchKeywords() {
        return reader.readSheet(Constants.SHEET_SEARCH_KEYWORDS, 1);
    }

    /**
     * Two columns: [searchKeyword, expectedProductName]
     * Used by cart-verification and favorites-verification tests.
     * The test searches for the keyword, picks the first result, and then
     * verifies the product name in the cart / favorites list.
     */
    @DataProvider(name = "cartProducts")
    public Object[][] provideCartProducts() {
        return reader.readSheet(Constants.SHEET_CART_PRODUCTS, 2);
    }

    // ─── Profile DataProviders ────────────────────────────────────────────────

    /**
     * Two columns: [newFirstName, newLastName]
     * Used by the UserProfile update tests.
     */
    @DataProvider(name = "profileUpdates")
    public Object[][] provideProfileUpdates() {
        return reader.readSheet(Constants.SHEET_PROFILE_UPDATES, 2);
    }
}
