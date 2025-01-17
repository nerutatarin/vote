package service.pagemanager;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import service.configurations.MemberConfig;
import service.configurations.VoteConfig;
import service.configurations.VoteMode;
import service.memberRank.MemberRanks;
import service.memberRank.Ranker;
import service.pagemanager.model.Member;
import service.pagemanager.model.VotingPage;
import service.webdriver.model.Process;
import utils.Utils;
import utils.WriteToLog;
import utils.ipaddress.model.IPAddress;
import utils.jackson.JsonMapper;
import votes.kp.PageManagerKP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.time.Duration.ofSeconds;
import static org.apache.log4j.Logger.getLogger;
import static org.jsoup.Jsoup.parse;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static utils.Thesaurus.FilesNameJson.*;
import static utils.Utils.*;
import static utils.jackson.JsonMapper.objectToFilePretty;

public abstract class PageManagerImpl implements PageManager {
    private static final Logger log = getLogger(PageManagerKP.class);

    protected WebDriverWait wait;
    protected WebDriver webDriver;
    protected Process process;
    protected String browserName;
    protected List<service.configurations.Member> members;
    protected List<String> inputs = new ArrayList<>();
    protected MemberConfig memberConfig;

    public PageManagerImpl(WebDriver webDriver, Process process, List<service.configurations.Member> members) {
        this.webDriver = webDriver;
        this.process = process;
        this.browserName = process.getBrowserName();
        this.memberConfig = getMemberConfig();
        this.members = members;
    }

    public PageManagerImpl(WebDriver webDriver, Process process) {
        this.webDriver = webDriver;
        this.process = process;
        this.browserName = process.getBrowserName();
        this.memberConfig = getMemberConfig();
        this.members = new ArrayList<>();
    }

    private MemberConfig getMemberConfig() {
        return new MemberConfig().parse();
    }

    public void votePage(String baseUrl) {
        log.info(browserName + " Запуск страницы голосования " + baseUrl);

        int timeout = 30;
        wait = new WebDriverWait(webDriver, ofSeconds(timeout));
        webDriver.get(baseUrl);

        sleep(2000);
        log.info(browserName + " UserAgent = " + getUserAgent(webDriver));

        saveCookie(COOKIE_AFTER_VOTING_JSON);
    }

    protected abstract VotingPage getPageBeforeVoting(Document pageSource);

    @NotNull
    protected abstract String getPageTitle();

    public void voteInput() {
        allowInputs();
        if (Utils.nullOrEmpty(inputs)) return;

        for (String inp : inputs) {
            log.info(browserName + " Ищем " + inp + " ...");
            WebElement webElement = wait.until(elementToBeClickable(id(inp)));
            webElement.click();
            log.info(browserName + " Проставлен " + inp);
        }
    }

    protected abstract void allowInputs();

    public void voteButton() {
        log.info(browserName + " Ищем кнопку голосования: ");
        WebElement webElement = wait.until(elementToBeClickable(getButtonLocator()));
        webElement.click();
        log.info(browserName + " Кнопка голосования нажата: ");

        sleep(2000);

        saveCookie(COOKIE_BEFORE_VOTING_JSON);
    }

    protected abstract By getButtonLocator();

    private void saveCookie(String fileName) {
        log.info(browserName + " " + "Пытаемся получить куки...");

        Set<Cookie> cookies = webDriver.manage().getCookies();
        if (cookies.size() == 0) {
            log.info(browserName + " " + "Не удалось получить куки!");
            return;
        }

        objectToFilePretty(cookies, fileName);
    }

    public void voteLogging(IPAddress IPAddress) {
        Document pageSource = getPageSource();

        VotingPage votingPage = getPageAfterVoting(pageSource);
        List<Member> memberList = votingPage.getMemberList();

        if (nullOrEmpty(memberList)) return;

        writeMemberRanks();

        for (Member member : memberList) {

            if (nullOrEmpty(inputs)) return;

            if (inputs.contains(member.getInput())) {
                log.info(browserName + " " + member);
                String ip = IPAddress.getIp();
                String country = IPAddress.getCountry();

                String title = member.getTitle();
                int count = member.getCount();

                WriteToLog writeToLog = new WriteToLog(browserName, title);
                writeToLog.ipCountryCount(ip, country, count);
            }
        }
    }

    private void writeMemberRanks() {
        VoteConfig voteConfig = new VoteConfig().parse();

        VoteMode voteMode = voteConfig.getVoteMode();

        Ranker ranker = new Ranker(voteMode, getAllowMembers());
        MemberRanks memberRanks = ranker.init();
        JsonMapper.objectToFilePretty(memberRanks, MEMBER_RANKS_JSON);
    }

    protected abstract Map<String, String> getAllowMembers();

    protected abstract VotingPage getPageAfterVoting(Document pageSource);

    protected Document getPageSource() {
        Boolean isPageLoaded = wait.until(ExpectedConditions.titleIs(getPageTitle()));
        if (!isPageLoaded) throw new TimeoutException();

        return parse(webDriver.getPageSource());
    }
}
