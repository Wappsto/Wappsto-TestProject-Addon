package actions;

import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.rest.network.*;
import wappsto.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;
import wappsto.session.model.*;

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

    public static CreatorResponse getCreator(WebDriver browser, String serviceUrl) throws FailureException {
        String sessionId;
        RestUser session;
        try {
            sessionId = getSessionFrom(browser);
            session = new RestUser(sessionId, serviceUrl);
        } catch (NoSuchCookieException e) {
            throw new FailureException("Browser is not logged in");
        } catch (HttpException e) {
            throw new FailureException(
                "Failed to create user session from session cookie"
            );
        } catch (Exception e) {
            throw new FailureException("Unknown error: " + e.getMessage());
        }
        RestNetworkService service = new RestNetworkService(session);
        CreatorResponse creator;
        try {
            creator = service.getCreator();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to get creator: " + e.getMessage()
            );
        }
        return creator;
    }

    public static RestUser createRestSession(
        String sessionId,
        String target
    )
        throws FailureException
    {
        try {
            return new RestUser(sessionId, target);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to create user session: " + e.getMessage()
            );
        }
    }

}
