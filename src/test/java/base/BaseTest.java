package base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.SelectOption;
import extentlisteners.ExtentListeners;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class BaseTest {
    private Playwright playwright;
    public Browser browser;
    public Page page;
    private static Properties OR = new Properties();
    private static Properties config = new Properties();

    private FileInputStream fis;
    private Logger log = Logger.getLogger(this.getClass());

    private static ThreadLocal<Playwright> pw = new ThreadLocal<>();
    private static ThreadLocal<Browser> br = new ThreadLocal<>();
    private static ThreadLocal<Page> pa = new ThreadLocal<>();

    public static Playwright getPlaywright() {
        return pw.get();
    }

    public static Browser getBrowser() {
        return br.get();
    }

    public static Page getPage() {
        return pa.get();
    }


    @BeforeSuite
    public void setUp() {
        PropertyConfigurator.configure("./src/test/resources/properties/log4j.properties");
        log.info("Test Execution started !!!");
        try {
            fis = new FileInputStream("./src/test/resources/properties/OR.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            OR.load(fis);
            log.info("OR properties file loaded");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void click(String locatorKey) {
        try {
            getPage().locator(OR.getProperty(locatorKey)).click();
            log.info("Clicking on an element : " + locatorKey);
            ExtentListeners.getExtent().info("Clicking on an element : " + locatorKey);

        } catch (Throwable t) {
            log.error("Error while clicking on an Element : " + t.getMessage());
            ExtentListeners.getExtent().fail("Error while clicking on an Element : " + t.getMessage());
            Assert.fail(t.getMessage());

        }
    }


    public boolean isElementPresent(String locatorKey) {
        try {
            //Dynamic wait
            getPage().waitForSelector(OR.getProperty(locatorKey), new Page.WaitForSelectorOptions().setTimeout(2000));
            getPage().locator((OR.getProperty(locatorKey))).isVisible();
            log.info("Element found : " + locatorKey);
            ExtentListeners.getExtent().info("Element found : " + locatorKey);
            return true;

        } catch (Throwable t) {
            ExtentListeners.getExtent().fail("Element not found : " + locatorKey);
            return false;
        }
    }


    public void type(String locatorKey, String value) {
        try {
            getPage().locator(OR.getProperty(locatorKey)).fill(value);
            log.info("Typing in an element : " + locatorKey + " and entered the value as : " + value);
            ExtentListeners.getExtent().info("Typing in an element : " + locatorKey + " and entered the value as : " + value);

        } catch (Throwable t) {
            log.error("Error while typing in an Element : " + t.getMessage());
            ExtentListeners.getExtent().fail("Error while typing in an Element : " + t.getMessage());
            Assert.fail(t.getMessage());
        }
    }

    public void select(String locatorKey, String value) {
        try {
            getPage().selectOption(OR.getProperty(locatorKey), new SelectOption().setLabel(value));
            log.info("Selecting an element : " + locatorKey + " and selected the value as : " + value);
            ExtentListeners.getExtent().info("Selecting an element : " + locatorKey + " and selected the value as : " + value);

        } catch (Throwable t) {
            log.error("Error while selecting an Element : " + t.getMessage());
            ExtentListeners.getExtent().fail("Error while selecting an Element : " + t.getMessage());
            Assert.fail(t.getMessage());
        }
    }

    public Browser getBrowser(String browserName) {

        playwright = Playwright.create();
        pw.set(playwright);
        switch (browserName) {
            case "chrome":
                log.info("Launching chrome browser");
                return getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
            case "headless":
                log.info("Launching headless mode");
                return getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            case "firefox":
                log.info("Launching firefox browser");
                return getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setChannel("firefox").setHeadless(false));
            case "webkit":
                log.info("Launching Webkit browser");
                return getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            default:
                throw new IllegalArgumentException();
        }
    }

    public void navigate(Browser browser, String url) {
        this.browser = browser;
        br.set(browser);
        page = getBrowser().newPage();
        pa.set(page);
        getPage().navigate(url);
        log.info("Navigated to : " + url);

        page.onDialog(dialog -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            dialog.accept();

            System.out.println(dialog.message());
        });

    }

    @AfterMethod
    public void quit() {
        if (getPage() != null) {
            getBrowser().close();
            getPage().close();
        }
    }

    @AfterSuite
    public void quitPlaywright() {
        if (getPage() != null) {
            getPlaywright().close();
        }
    }
}

