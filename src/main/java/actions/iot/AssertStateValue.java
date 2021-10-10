package actions.iot;

import actions.*;
import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.api.network.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.session.*;

import java.util.*;

import static actions.Utils.*;

@Action(name = "Assert value of State")
public class AssertStateValue implements WebAction {
    @Parameter(description = "State UUID")
    public String state;

    @Parameter(description = "Expected value")
    public String expected;

    @Parameter(
        description = "Service API Root",
        defaultValue = Defaults.SERVICE_URL
    )
    public String serviceUrl;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId;
        Controller controller;
        try {
            sessionId = getSessionFrom(browser);
            controller = new Controller(
                sessionId,
                serviceUrl,
                state,
                expected
            );
        } catch (NoSuchCookieException e) {
            throw new FailureException("Session cookie not found");
        } catch (Exception e) {
            throw new FailureException(
                "Failed to create session: " + e.getMessage()
            );
        }
        controller.execute();
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        public final NetworkService service;
        private final String state;
        private final String expected;

        public Controller(
            String sessionId,
            String target,
            String state,
            String expected
        )
            throws Exception
        {
            this(
                new RestNetworkService(new RestUser(sessionId, target)),
                state,
                expected
            );
        }

        public Controller(
            NetworkService service,
            String state,
            String expected
        ) {
            this.service = service;
            this.state = state;
            this.expected = expected;
        }

        public void execute() throws FailureException {
            try {
                assert service
                    .getState(UUID.fromString(state))
                    .equals(expected) : "State did not match expected value";
            } catch (AssertionError e) {
                throw new FailureException("Assertion error: " + e.getMessage());
            } catch (Exception e) {
                throw new FailureException(
                    "Error reading state: " + e.getMessage()
                );
            }
        }
    }
}
