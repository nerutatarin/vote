package vote.browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import utils.RandomUserAgent;

import java.time.Duration;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.PageLoadStrategy.EAGER;
import static utils.Thesaurus.Capabilities.MOZ_PROCESS_ID;
import static utils.Thesaurus.Drivers.GECKO_DRIVER_VALUE;

public class Firefox extends BrowsersImpl {

    @Override
    protected void webDriverInitialize() {
        WebDriverManager.firefoxdriver().setup();
    }

    @Override
    protected String getProcessId() {
        return getCapabilities().getCapability(MOZ_PROCESS_ID).toString();
    }

    @Override
    protected String getDriverName() {
        return GECKO_DRIVER_VALUE;
    }

    @Override
    protected WebDriver getDriverInstance() {
        return new FirefoxDriver(getOptions());
    }

    private FirefoxOptions getOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(new FirefoxProfile());

        options.addArguments("-private");

        String userAgent = RandomUserAgent.getRandomUserAgent();
        options.addPreference("general.useragent.override", userAgent);
        //options.addArguments("user-agent=" + userAgent);
        options.addPreference("dom.webdriver.enabled", false);
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("useAutomationExtension", false);
        options.addPreference("toolkit.startup.max_resumed_crashes", "-1");

        Duration timeout = ofSeconds(30);
        options.setPageLoadTimeout(timeout);
        options.setImplicitWaitTimeout(timeout);
        options.setScriptTimeout(timeout);

        options.setHeadless(true);
        options.setAcceptInsecureCerts(true);
        options.setPageLoadStrategy(EAGER);
        //options.setProxy(getProxy());
        return options;
    }
}
