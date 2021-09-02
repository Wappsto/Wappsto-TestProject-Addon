package test;

import io.testproject.java.enums.AutomatedBrowserType;
import io.testproject.java.sdk.v2.Runner;
import io.testproject.java.sdk.v2.internal.DriverHelper;
import org.openqa.selenium.WebDriver;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.model.Credentials;
import wappsto.rest.session.AdminSession;

import java.io.IOException;


public class Utils {
    private static AdminSession admin;
    private static Runner runner;

    public static Credentials defaultUser() {
        return new Credentials(
            "test123123@seluxit.com",
            "123"
        );
    }

    public static AdminSession admin() throws Exception {
        Config tc = new Config();
        if (admin == null) {
            admin = new AdminSession(
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
}