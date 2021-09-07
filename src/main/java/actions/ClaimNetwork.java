package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.WebDriver;
import wappsto.rest.session.UserSession;

@Action(name = "Claim Network")
public class ClaimNetwork implements WebAction {
    @Parameter(description = "Service API Root")
    public String serviceUrl;
    @Parameter(description = "Network ID")
    public String networkId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId = browser
            .manage()
            .getCookieNamed("sessionID")
            .getValue();

        UserSession session;
        try {
            session = new UserSession(
                sessionId,
                serviceUrl
            );
        } catch (Exception e) {
            throw new FailureException(
                "Failed to get session ID: " + e.getMessage()
            );
        }
        try {
            session.claimNetwork(networkId.trim());
        } catch (Exception e) {
            throw new FailureException(
                "Failed to claim network with Id: " + networkId + "\n" +
                    e.getMessage()
            );
        }

        return ExecutionResult.PASSED;
    }
}
