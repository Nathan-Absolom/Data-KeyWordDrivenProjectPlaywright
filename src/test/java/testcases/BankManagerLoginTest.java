package testcases;

import base.BaseTest;
import com.microsoft.playwright.Browser;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.Constants;

public class BankManagerLoginTest extends BaseTest {
    @Test
    public void loginAsBankManager() {

        Browser browser = getBrowser("chrome");
        navigate( browser  , Constants.URL);

        click("bmlBtn");
        Assert.assertTrue(isElementPresent("addCustBtn"), "Bank Manager not logged in");
    }
}
