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

import static actions.Utils.createRestSession;
import static actions.Utils.getSessionFrom;

@Action(name = "Change value of control state")
public class ChangeControlState implements WebAction {
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
        new Controller(
            sessionId,
            serviceUrl,
            controlState,
            value
        ).execute();

        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final NetworkService service;
        private final String controlState;
        private final String value;

        public Controller(
            String sessionId,
            String target,
            String controlState,
            String value
        )
            throws FailureException
        {
            this(
                new RestNetworkService(createRestSession(sessionId, target)),
                controlState,
                value
            );
        }

        public Controller(
            NetworkService service,
            String controlState,
            String value
        ) {
            this.service = service;
            this.controlState = controlState;
            this.value = value;
        }

        public void execute() throws FailureException {
            try {
                service.updateState(UUID.fromString(controlState), value);
            } catch (Exception e) {
                throw new FailureException(
                    "Error updating state: " + e.getMessage()
                );
            }
        }
    }
}
