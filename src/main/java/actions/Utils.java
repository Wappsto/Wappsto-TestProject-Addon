package actions;

import org.openqa.selenium.*;

public class Utils {
    public static void logIn(WebDriver browser, String session) {
        browser.manage().addCookie(
            new Cookie("sessionID", session)
        );
    }
}
