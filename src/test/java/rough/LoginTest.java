package rough;

import base.BaseTest;
import com.microsoft.playwright.Browser;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void doLogin() {
        Browser browser = getBrowser("chrome");
        navigate( browser  ,"http://google.com");
        type("searchBox", "Hello Playwright");
    }

    @Test
    public void doGmailLogin(){
        Browser browser = getBrowser("firefox");
        navigate( browser  ,"http://gmail.com");
        type("username", "trainer@way2automation.com");

    }

}
