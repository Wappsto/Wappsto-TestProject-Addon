package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.iot.network.*;
import wappsto.iot.rpc.model.*;
import wappsto.rest.network.model.*;

import static actions.Utils.createVirtualIoTNetwork;
import static actions.Utils.getCreator;

@Action(name = "Send report state change to dashboard")
public class SendReportStateChangeToDashboard implements WebAction {
    @Parameter(description = "Service API root")
    public String serviceUrl;

    @Parameter(description = "Socket URL")
    public String socketUrl;

    @Parameter(description = "Socket port")
    public String port;

    @Override
    public ExecutionResult execute(WebAddonHelper helper)
        throws FailureException
    {
        WebDriver browser = helper.getDriver();
        CreatorResponse creator = getCreator(browser, serviceUrl);
        VirtualIoTNetwork network = createVirtualIoTNetwork(
            creator,
            0,
            1,
            1,
            "Boolean",
            port,
            socketUrl
        );
        network.update(new ControlStateData(
            network.getControlState(0),
            "1"
        ));
        return ExecutionResult.PASSED;
    }
}
