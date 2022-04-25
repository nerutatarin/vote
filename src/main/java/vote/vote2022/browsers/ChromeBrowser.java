package vote.vote2022.browsers;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import vote.vote2022.driver.Driver;

import static java.lang.System.out;
import static org.apache.log4j.Logger.getLogger;
import static org.openqa.selenium.PageLoadStrategy.NORMAL;
import static org.openqa.selenium.remote.CapabilityType.*;
import static utils.Thesaurus.Drivers.CHROME_DRIVER_KEY;
import static utils.Thesaurus.Drivers.CHROME_DRIVER_VALUE;

public class ChromeBrowser extends Browsers {
    private static final Logger log = getLogger(ChromeBrowser.class);

    @Override
    protected void setDriverProperty() {
        out.println("Init Chrome Drivers...");
        log.info("Init Chrome Drivers...");
        Driver driver = new Driver(CHROME_DRIVER_KEY, CHROME_DRIVER_VALUE);
        driver.setPropertyDependsOnOS();
    }

    @Override
    protected String getProcessId() {
        return null;
    }

    @Override
    protected String getDriverName() {
        return CHROME_DRIVER_VALUE;
    }

    @Override
    protected WebDriver getDriverInstance() {
        return new ChromeDriver(getOptions());
    }

    private ChromeOptions getOptions() {
        log.info("Chrome options...");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--enable-automation");
        options.addArguments("--headless");
        options.addArguments("--incognito");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-ssl-errors");
        options.addArguments("--disable-extensions");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--enable-precise-memory-info");

        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(NORMAL);

        //options.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true);
        options.setCapability(SUPPORTS_JAVASCRIPT, true);
        options.setCapability(ELEMENT_SCROLL_BEHAVIOR, true);
        options.setCapability(HAS_NATIVE_EVENTS, true);
        options.setCapability(PROXY, getProxy());
        return options;
    }
}
