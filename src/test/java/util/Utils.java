package util;

import io.testproject.java.enums.AutomatedBrowserType;
import io.testproject.java.sdk.v2.Runner;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import wappsto.rest.session.Admin;
import wappsto.rest.session.model.AdminCredentials;
import wappsto.rest.session.model.Credentials;


public class Utils {
    private static Admin admin;
    private static Runner runner;

    public static Credentials defaultUser() {
        return new Credentials(
            "test123123@seluxit.com",
            "123"
        );
    }

    public static Admin admin() throws Exception {
        Config tc = new Config();
        if (admin == null) {
            admin = new Admin(
                new AdminCredentials(
                    tc.ADMIN_USERNAME,
                    tc.ADMIN_PASSWORD
                ),
                tc.API_ROOT
            );
        }
        return admin;
    }

    public static Runner runner() throws Exception {
        if (runner == null) {
            runner = Runner.createWeb(
                new Config().DEV_TOKEN,
                AutomatedBrowserType.Chrome);
        }

        return runner;
    }

    public static void resetRunner() throws Exception {
        WebDriver driver = runner().getDriver();
        driver.manage().deleteAllCookies();
    }

    public static void logInBrowser(
        String sessionId,
        String appUrl
    ) throws Exception {
        WebDriver browser = runner().getDriver();
        browser.navigate().to(appUrl);
        browser.manage().addCookie(
            new Cookie("sessionID", sessionId)
        );
    }
}
