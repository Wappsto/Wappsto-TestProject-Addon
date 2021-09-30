package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.network.*;
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
        Controller presenter;
        try {
            sessionId = getSessionFrom(browser);
            presenter = new Controller(
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

        try {
             presenter.execute();
        } catch (Exception e) {
            throw new FailureException(e.getMessage());
        }

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

        public void execute() throws Exception {
            assert service
                .getState(UUID.fromString(state))
                .equals(expected) : "State did not match expected value";
        }
    }
}
