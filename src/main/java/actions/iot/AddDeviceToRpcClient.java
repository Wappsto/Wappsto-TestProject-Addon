package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.iot.*;

public class AddDeviceToRpcClient
    extends ActionWithSSLSocket
    implements WebAction
{
    @Parameter(description = "Device name")
    public String name;
    @Parameter(
        description = "Device UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String deviceId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        return null;
    }

    public static class Controller {
        public Controller(
            String networkId,
            DataStore store,
            Connection connection
        ) {

        }

        public String execute() throws FailureException {
            throw new FailureException("");
        }
    }
}
