package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

import java.util.*;

import static actions.Utils.getSessionFrom;

@Action(name = "Change value of control state")
public class ChangeControlStateValue implements WebAction {
    @Parameter(description = "Control state UUID")
    public String controlState;

    @Parameter(description = "Value")
    public String value;

    @Parameter(description = "Service API Root")
    public String serviceUrl;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId;
        try {
            sessionId = getSessionFrom(browser);
        } catch (NoSuchCookieException e) {
            throw new FailureException("Browser not logged in");
        }
        RestUser session;
        NetworkService service;
        try {
            session = new RestUser(sessionId, serviceUrl);
            service = new NetworkService(session);
            service.updateState(UUID.fromString(controlState), value);
        } catch (Exception e) {
            throw new FailureException("Error updating state: " + e.getMessage());
        }

        return ExecutionResult.PASSED;
    }
}
