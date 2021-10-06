package actions.network;

import actions.*;
import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.drivers.WebDriver;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.network.*;
import wappsto.rest.network.*;

import static actions.Utils.*;

@Action(name = "Share network")
public class ShareNetwork implements WebAction {
    @Parameter(description = "Network UUID")
    public String network;

    @Parameter(
        description = "Application API URL",
        defaultValue = Defaults.APPLICATION_URL
    )
    public String serviceUrl;

    @Parameter(description = "Other username")
    public String other;

    @Override
    public ExecutionResult execute(WebAddonHelper helper)
        throws FailureException
    {
        WebDriver browser = helper.getDriver();
        String sessionId;
        try {
            sessionId = getSessionFrom(browser);
        } catch (NoSuchCookieException e) {
            throw new FailureException(
                "Browser not logged in: " + e.getMessage()
            );
        }
        new Controller(
            sessionId,
            serviceUrl,
            network,
            other
        ).execute();

        return ExecutionResult.PASSED;
    }

    public static class Controller {

        private final NetworkService service;
        private final String network;
        private final String other;

        public Controller(
            String sessionId,
            String target,
            String network,
            String other
        ) throws FailureException {
            this(
                new RestNetworkService(createRestSession(sessionId, target)),
                network,
                other
            );
        }

        public Controller(
            NetworkService service,
            String network,
            String other
        ) {

            this.service = service;
            this.network = network;
            this.other = other;
        }

        public void execute() throws FailureException {
            try {
                service.share(network, other);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to share network: " + e.getMessage()
                );
            }
        }
    }
}
