package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.iot.network.*;
import wappsto.rest.network.model.*;

import static actions.Utils.createVirtualIoTNetwork;
import static actions.Utils.getCreator;

@Action(
    name = "Run RPC Client",
    description = "Create and run a simple virtual IoT network"
)
public class RunSimpleRPCClient implements WebAction {
    @Parameter(description = "Service API root")
    public String serviceUrl;

    @Parameter(description = "Socket URL")
    public String socketUrl;

    @Parameter(description = "Socket port")
    public String port;

    @Parameter(description = "Minimum value")
    public String min;

    @Parameter(description = "Maximum value")
    public String max;

    @Parameter(description = "Step size")
    public String stepSize;

    @Parameter(description = "Value type")
    public String type;

    @Parameter(
        description = "Report state UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String reportState;

    @Parameter(
        description = "Control state UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String controlState;

    @Parameter(
        description = "Network UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String networkId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper)
        throws FailureException
    {
        WebDriver browser = helper.getDriver();
        CreatorResponse creator = getCreator(browser, serviceUrl);
        VirtualIoTNetwork network = createVirtualIoTNetwork(
            creator,
            Integer.parseInt(min),
            Integer.parseInt(max),
            Integer.parseInt(stepSize),
            type,
            port, socketUrl
        );

        reportState = network.getReportState(0).toString();
        controlState = network.getControlState(0).toString();
        networkId = creator.network.id;
        return ExecutionResult.PASSED;
    }
}
