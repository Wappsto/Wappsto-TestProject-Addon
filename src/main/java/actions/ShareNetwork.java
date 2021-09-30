package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.drivers.WebDriver;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.rest.network.*;
import wappsto.rest.session.*;

import static actions.Utils.*;

@Action(name = "Share network")
public class ShareNetwork implements WebAction {
    @Parameter(description = "Network UUID")
    public String network;

    @Parameter(description = "Application API URL")
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

        RestUser self;
        try {
            self = new RestUser(sessionId, serviceUrl);
            NetworkService service = new NetworkService(self);
            service.share(network, other);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to share network: " + e.getMessage()
            );
        }

        return ExecutionResult.PASSED;
    }
}
