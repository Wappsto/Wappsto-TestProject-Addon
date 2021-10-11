package actions.network;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.api.network.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.session.*;

import static actions.Utils.*;

@Action(name = "Create device under network")
public class CreateDeviceUnderNetwork implements WebAction {
    @Parameter(description = "Service API URL")
    public String serviceUrl;

    @Parameter(description = "Network UUID")
    public String networkId;

    @Parameter(
        description = "Device UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String deviceId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        WebDriver browser = helper.getDriver();
        String sessionId = getSessionFrom(browser);

        deviceId = new Controller(serviceUrl, sessionId, networkId).execute();

        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final NetworkService service;
        private final String networkId;

        public Controller(NetworkService service, String networkId) {

            this.service = service;
            this.networkId = networkId;
        }

        public Controller(
            String serviceUrl,
            String networkId,
            String sessionId
        )
            throws FailureException
        {
            this(
                createRestService(serviceUrl, sessionId),
                networkId
            );
        }

        private static RestNetworkService createRestService(
            String serviceUrl,
            String sessionId
        )
            throws FailureException
        {
            try {
                return new RestNetworkService(new RestUser(sessionId, serviceUrl));
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to create network service: " + e.getMessage()
                );
            }
        }

        public String execute() throws FailureException {
            try {
                return service.createDevice(networkId);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to create device: " + e.getMessage()
                );
            }
        }
    }
}
