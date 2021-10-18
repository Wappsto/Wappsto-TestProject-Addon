package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;

@Action(name = "Change value type of existing value on RPC client")
public class ChangeValueType extends ActionWithSSLSocket implements WebAction {
    @Parameter(description = "Network UUID")
    public String networkId;

    @Parameter(description = "Value UUID")
    public String valueId;

    @Parameter(description = "Name")
    public String name;

    @Parameter(description = "Type")
    public String type;

    @Parameter(description = "Minimum")
    public String min;

    @Parameter(description = "Maximum")
    public String max;

    @Parameter(description = "Step size")
    public String stepSize;

    @Parameter(description = "Permissions (r/w/rw")
    public String permissions;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        return null;
    }
}
