package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.rest.network.*;
import wappsto.rest.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import static actions.Utils.getSessionFrom;

@Action(name = "Observe report state change")
public class ObserveReportStateChangeInDashboard implements WebElementAction {
    @Parameter(description = "Service API root")
    public String serviceUrl;

    @Parameter(description = "Socket URL")
    public String socketUrl;

    @Parameter(description = "Socket port")
    public String port;

    @Override
    public ExecutionResult execute(WebAddonHelper helper, WebElement element)
        throws FailureException
    {
        WebDriver browser = helper.getDriver();
        String sessionId;
        User session;
        try {
            sessionId = getSessionFrom(browser);
            session = new User(sessionId, serviceUrl);
        } catch (NoSuchCookieException e) {
            throw new FailureException("Browser is not logged in");
        } catch (HttpException e) {
            throw new FailureException(
                "Failed to create user session from session cookie"
            );
        } catch (Exception e) {
            throw new FailureException("Unknown error: " + e.getMessage());
        }
        NetworkService service = new NetworkService(session);
        CreatorResponse creator;
        try {
            creator = service.getCreator();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to get creator: " + e.getMessage()
            );
        }
        int socketPort = Integer.parseInt(port);

        RPCClient client;
        try {
            client = new RPCClient.Builder(creator)
                .connectingTo(socketUrl, socketPort)
                .build();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to connect to socket: " + e.getMessage()
            );
        }

        NetworkSchema schema = new NetworkSchema.Builder(
            "Test",
            creator.network.id
        ).addDevice("Test")
            .addValue("Test", ValuePermission.RW)
            .withNumberSchema(0, 1, 1, "Boolean")
            .addToDevice()
            .addToNetwork()
            .build();

        VirtualIoTNetwork network;
        try {
            network = new VirtualIoTNetwork(schema, client);
        } catch (Exception e) {
            throw new FailureException(
                "Failed to start client: " + e.getMessage()
            );
        }
        network.update(new ControlStateData(
            network.getControlState(0),
            "1"
        ));

        if (element.getText().equals("1")) {
            return ExecutionResult.PASSED;
        } else {
            return ExecutionResult.FAILED;
        }
    }
}
