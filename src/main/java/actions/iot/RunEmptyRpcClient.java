package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.filesystem.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;

import static actions.Utils.*;

@Action(name = "Run RPC Client with no devices")
public class RunEmptyRpcClient extends ActionWithSSLSocket implements WebAction {
    @Parameter(description = "Network name")
    public String name;

    @Parameter(
        description = "Network UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String networkId;

    @Parameter(description = "Service API URL")
    public String serviceUrl;

    @Parameter(
        description = "Manufacturer as owner, (true/false)"
    )
    public String manufacturerAsOwner;


    @Override
    public ExecutionResult execute(WebAddonHelper helper)
        throws FailureException
    {
        WebDriver browser = helper.getDriver();
        CreatorResponse creator = getCreator(
            browser,
            serviceUrl,
            Boolean.parseBoolean(manufacturerAsOwner)
        );
        Controller controller;
        try {
            controller = new Controller(
                name,
                creator,
                Integer.parseInt(port),
                socketUrl
            );
        } catch (Exception e) {
            throw new FailureException(
                "Failed to establish socket connection: " + e.getMessage()
            );
        }

        networkId = controller.execute();
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final String name;
        private final CreatorResponse creator;
        private final Connection connection;
        private final DataStore store;

        public Controller(
            String name,
            CreatorResponse creator,
            int port,
            String socketUrl
        ) throws Exception {
            this(
                name,
                creator,
                new SSLConnection(socketUrl, port, new WappstoCerts(creator)),
                new FileSystemJsonDataStore("./saved_instance/")
            );
        }

        public Controller(
            String name,
            CreatorResponse creator,
            Connection connection,
            DataStore store
        ) {

            this.name = name;
            this.creator = creator;
            this.connection = connection;
            this.store = store;
        }

        public String execute() {
            NetworkSchema schema = new NetworkSchema(name, creator.network.id);
            VirtualIoTNetwork network = new VirtualIoTNetwork(
                schema,
                new RpcClient(connection)
            );
            store.save(
                schema.meta.id.toString(),
                new NetworkInstance(new WappstoCerts(creator), schema)
            );
            return schema.meta.id.toString();
        }
    }
}
