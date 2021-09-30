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

import static actions.Utils.*;

@Action(name = "Assert value of State")
public class AssertStateValue implements WebAction {
    @Parameter(description = "State UUID")
    public String state;

    @Parameter(description = "Expected value")
    public String expected;

    @Parameter(description = "Service API Root")
    public String serviceUrl;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId;
        RestUser session;
        NetworkService service;
        try {
            sessionId = getSessionFrom(browser);
            session = new RestUser(sessionId, serviceUrl);
            service = new NetworkService(session);
        } catch (NoSuchCookieException e) {
            throw new FailureException("Session cookie not found");
        } catch (Exception e) {
            throw new FailureException(
                "Failed to create session: " + e.getMessage()
            );
        }

        try {
            assert service.getState(UUID.fromString(state)).equals(expected) :
                "State did not match expected value";
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }

        return ExecutionResult.PASSED;
    }
}
