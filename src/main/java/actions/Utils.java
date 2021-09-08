package actions;

import org.openqa.selenium.*;

public class Utils {
    public static void logIn(WebDriver browser, String session) {
        browser.manage().addCookie(
            new Cookie("sessionID", session)
        );
    }

    public static String getSessionFrom(WebDriver browser) {
        try {
            return browser
                .manage()
                .getCookieNamed("sessionID")
                .getValue();
        } catch (NullPointerException e) {
            throw new NoSuchCookieException("Session cookie not found");
        }
    }
}
