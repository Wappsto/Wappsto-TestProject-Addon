package actions.network;

import actions.*;
import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.api.network.*;
import wappsto.api.rest.network.*;

import static actions.Utils.*;

@Action(name = "Claim Network")
public class ClaimNetwork implements WebAction {
    @Parameter(
        description = "Service API Root",
        defaultValue = Defaults.SERVICE_URL
    )
    public String serviceUrl;
    @Parameter(description = "Network ID")
    public String networkId;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId;
        try {
            sessionId = getSessionFrom(browser);
        } catch (NoSuchCookieException e) {
            throw new FailureException("Browser not logged in");
        }

        new Controller(sessionId, serviceUrl, networkId).execute();


        return ExecutionResult.PASSED;
    }


    public static class Controller {
        private NetworkService service;
        private String network;

        public Controller(
            String sessionId,
            String target,
            String network
        ) throws FailureException {
            this(
                new RestNetworkService(createRestSession(sessionId, target)),
                network
            );
        }
        public Controller(NetworkService service, String network) {
            this.service = service;
            this.network = network;
        }

        public void execute() throws FailureException {
            try {
                service.claim(network);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to claim network with ID: " + network + "\n" +
                        e.getMessage()
                );
            }
        }
    }
}
