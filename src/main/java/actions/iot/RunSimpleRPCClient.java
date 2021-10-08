package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.iot.*;
import wappsto.iot.filesystem.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;
import wappsto.network.model.*;

import static actions.Utils.*;

@Action(
    name = "Run RPC Client",
    description = "Create and run a simple virtual IoT network"
)
public class RunSimpleRPCClient extends ActionWithSSLSocket implements WebAction {
    @Parameter(description = "Service API root")
    public String serviceUrl;

    @Parameter(description = "Minimum value")
    public String min;

    @Parameter(description = "Maximum value")
    public String max;

    @Parameter(description = "Step size")
    public String stepSize;

    @Parameter(description = "Value type")
    public String type;

    @Parameter(description = "Network name")
    public String name;

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
        Controller controller;
        try {
             controller = new Controller(
                name,
                creator,
                new NumberSchema(
                    Integer.parseInt(min),
                    Integer.parseInt(max),
                    Integer.parseInt(stepSize),
                    type
                ),
                Integer.parseInt(port),
                socketUrl
            );
        } catch (Exception e) {
            throw new FailureException(
                "Failed to establish socket connection: " + e.getMessage()
            );
        }
        VirtualIoTNetwork network = controller.execute();

        reportState = network.getReportState(0).toString();
        controlState = network.getControlState(0).toString();
        networkId = creator.network.id;
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final NetworkSchema schema;
        private final Connection connection;

        public Controller(
            String name,
            CreatorResponse creator,
            NumberSchema numbers,
            int port,
            String socketUrl
        ) throws Exception {
            this(
                name,
                creator,
                numbers,
                new SSLConnection(socketUrl, port, new WappstoCerts(creator)),
                new FileSystemJsonDataStore("./saved_instance/")
            );
        }

        public Controller(
            String name,
            CreatorResponse creator,
            NumberSchema numbers,
            Connection connection,
            DataStore store
        ) {
            this.connection = connection;
            schema = new NetworkSchema.Builder(name, creator.network.id)
                .addDevice("Test")
                .addValue("Test", ValuePermission.RW)
                .withNumberSchema(numbers)
                .addToDevice()
                .addToNetwork()
                .build();
            NetworkInstance instance = new NetworkInstance(
                new WappstoCerts(creator),
                schema
            );
            store.save(creator.network.id, instance);
        }

        public VirtualIoTNetwork execute() {
            return new VirtualIoTNetwork(
                schema,
                new RpcClient(connection)
            );
        }
    }
}
