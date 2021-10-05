package util;

import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.*;
import org.openqa.selenium.*;
import wappsto.iot.network.model.*;
import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

import java.util.*;

import static util.Env.*;


public class Utils {
    private static Runner runner;

    public static Credentials defaultUser() {
        return new Credentials(
            "test_123123@seluxit.com",
            "123"
        );
    }
    public static RestUser createNewUserSession(
        String serviceUrl,
        Admin admin
    ) {
        try {
            return new RestUser.Builder(admin, serviceUrl)
                .withCredentials(defaultUser())
                .create();
        } catch (Exception e) {
            try {
                return new RestUser(defaultUser(), serviceUrl);
            } catch (Exception exception) {
                throw new RuntimeException("Failed to create user session");
            }
        }
    }

    public static RestAdmin createNewAdmin() {
        try {
            return new RestAdmin(
                new AdminCredentials(
                    env().get(ADMIN_USERNAME),
                    env().get(ADMIN_PASSWORD)
                ),
                env().get(API_ROOT)
            );
        } catch (Exception e) {
            throw new RuntimeException(
                "Failed to log in admin: " + e.getMessage()
            );
        }
    }

    public static Runner runner() throws Exception {
        if (runner == null) {
            runner = Runner.createWeb(
                env().get(DEV_TOKEN),
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

    public static void logBrowserOut() throws Exception {
        WebDriver browser = runner().getDriver();
        browser.manage().deleteAllCookies();
    }

    public static NetworkSchema createNetworkSchema() {
        return new NetworkSchema.Builder(
            "On/off switch",
            UUID.randomUUID().toString()
        ).addDevice("Switch")
            .addValue("On/off", ValuePermission.RW)
            .withNumberSchema(0, 1, 1, "Boolean")
            .addToDevice()
            .addToNetwork()
            .build();
    }

}
