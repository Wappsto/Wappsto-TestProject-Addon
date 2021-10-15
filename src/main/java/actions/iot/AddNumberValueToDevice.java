package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;

public class AddNumberValueToDevice extends ActionWithSSLSocket implements WebAction {

    @Parameter(description = "Network UUID")
    public String networkId;

    @Parameter(description = "Device UUID")
    public String deviceId;

    @Parameter(description = "Value name")
    public String name;

    @Parameter(description = "Type")
    public String type;

    @Parameter(description = "Minimum value")
    public String min;

    @Parameter(description = "Maximum value")
    public String max;

    @Parameter(description = "Step size")
    public String stepSize;

    @Parameter(description = "Permissions (r, w, or rw")
    public String permissions;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        return null;
    }
}
