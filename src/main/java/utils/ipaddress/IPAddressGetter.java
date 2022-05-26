package utils.ipaddress;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import utils.ipaddress.model.MyIpAddress;
import vote.browsers.model.Process;

import static org.apache.log4j.Logger.getLogger;
import static org.jsoup.Jsoup.parseBodyFragment;
import static org.openqa.selenium.By.cssSelector;

public class IPAddressGetter {
    private static final Logger log = getLogger(IPAddressGetter.class);

    public static MyIpAddress getIpAddressLocator(WebDriver webDriver, Process process, String url) {
        log.info(process.getProcessName() + " Получаем IP адрес... ");

        String ipAddressLocator = "#ipcontent > table > tbody > tr:nth-child(2) > td";

        if (url.isEmpty()) {
            url = "https://myip.ru/";
        }

        try {
            webDriver.get(url);
            String ip = webDriver.findElement(cssSelector(ipAddressLocator)).getText();
            MyIpAddress myIpAddress = new MyIpAddress();
            myIpAddress.setIp(ip);
            return myIpAddress;
        } catch (Exception e) {
            log.error("Превышено время ожидания загрузки страницы: " + e);
        }

        return null;
    }

    public static MyIpAddress getIpAddressJson(WebDriver webDriver, Process process, String url) {
        log.info(process.getProcessName() + " Получаем IP адрес... ");

        if (url.isEmpty()) {
            url = "https://ipinfo.io/?token=c2e2408c951023";
        }

        try {
            webDriver.get(url);
            String pageSource = webDriver.getPageSource();
            String document = parseBodyFragment(pageSource).text();
            Gson gson = new Gson();
            MyIpAddress myIpAddress = gson.fromJson(document, MyIpAddress.class);
            return myIpAddress;
        } catch (Exception e) {
            log.error("Превышено время ожидания загрузки страницы: " + e);
        }

        return null;
    }

    public static MyIpAddress getIpAddress(WebDriver webDriver, Process process, String url) {
        log.info(process.getProcessName() + " Получаем IP адрес... ");

        if (url.isEmpty()) {
            url = "https://api.ipify.org/";
        }

        try {
            webDriver.get(url);
            String pageSource = webDriver.getPageSource();
            Document document = parseBodyFragment(pageSource);
            MyIpAddress myIpAddress = new MyIpAddress();
            myIpAddress.setIp(document.text());
            log.info(process.getProcessName() + "IP адрес " + myIpAddress.getIp());
            return myIpAddress;
        } catch (Exception e) {
            log.error("Превышено время ожидания загрузки страницы: " + e);
        }

        return null;
    }
}
