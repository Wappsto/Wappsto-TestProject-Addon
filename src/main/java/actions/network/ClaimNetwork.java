package actions.network;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.*;
import wappsto.network.*;
import wappsto.rest.network.RestNetworkService;
import wappsto.rest.session.RestUser;

import static actions.Utils.getSessionFrom;

@Action(name = "Claim Network")
public class ClaimNetwork implements WebAction {
    @Parameter(description = "Service API Root")
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

        try {
            new Controller(sessionId, serviceUrl, networkId)
                .execute();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to claim network with Id: " + networkId + "\n" +
                    e.getMessage()
            );
        }

        return ExecutionResult.PASSED;
    }


    public static class Controller {
        private NetworkService service;
        private String network;

        public Controller(
            String sessionId,
            String target,
            String network
        ) throws Exception {
            this(
                new RestNetworkService(new RestUser(sessionId, target)),
                network
            );
        }

        public Controller(NetworkService service, String network) {
            this.service = service;
            this.network = network;
        }

        public void execute() throws Exception {
            service.claim(network);
        }
    }
}
