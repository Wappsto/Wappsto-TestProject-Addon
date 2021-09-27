package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.drivers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
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
        String sessionId = getSessionFrom(browser);

        User self;
        try {
            self = new User(sessionId, serviceUrl);
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
