package testcases;

import base.BaseTest;
import org.testng.annotations.Test;
import utilities.Constants;
import utilities.DataProviders;
import utilities.DataUtil;
import utilities.ExcelReader;

import java.util.Hashtable;

public class OpenAccountTest extends BaseTest {
    @Test(dataProviderClass = DataProviders.class, dataProvider = "bankManagerDP")

    public void openAccountTest(Hashtable<String, String> data) {
        ExcelReader excel = new ExcelReader(Constants.SUITE1_XL_PATH);
        DataUtil.checkExecution("BankManagerSuite", "OpenAccountTest", data.get("Runmode"), excel);
        //To be removed
        System.out.println("Just a test");
        browser = getBrowser(data.get("browser"));
        navigate(browser, Constants.URL);
        click("bmlBtn");
        click("openAccount_btn");
        select("customer", data.get("customer"));
        select("currency", data.get("currency"));
        click("process_btn");


    }
}
