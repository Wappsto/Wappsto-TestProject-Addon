package actions.network;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.drivers.WebDriver;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

import static actions.Utils.createRestSession;
import static actions.Utils.getSessionFrom;

@Action(name = "Create new network")
public class CreateNetwork implements WebAction {
    @Parameter(description = "Service API root")
    public String serviceUrl;

    @Parameter(
        description = "Network UUID", direction = ParameterDirection.OUTPUT
    )
    public String network;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        String sessionId;
        try {
            WebDriver browser = helper.getDriver();
            sessionId = getSessionFrom(browser);
        } catch (NoSuchCookieException e) {
            throw new FailureException("Browser not logged in");
        }

        network = new Controller(sessionId, serviceUrl).execute();
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private NetworkService service;

        public Controller(
            String sessionId,
            String target
        ) throws FailureException
        {
            this(new RestNetworkService(createRestSession(sessionId, target)));
        }

        public Controller(NetworkService service) {
            this.service = service;
        }

        public String execute() throws FailureException {
            try {
                return service.create().id;
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to create network: " + e.getMessage()
                );
            }
        }
    }
}
