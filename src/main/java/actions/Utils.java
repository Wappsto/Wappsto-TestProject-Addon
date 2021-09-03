package actions;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class Utils {
    public static void logIn(WebDriver browser, String session) {
        browser.manage().addCookie(
            new Cookie("sessionID", session)
        );
    }
}
