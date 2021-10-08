package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.iot.*;
import wappsto.iot.filesystem.*;
import wappsto.iot.network.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.network.model.*;

@Action(
    name = "Load and run saved client",
    description = "This action may be removed in the future: it simply exists to" +
        "verify whether loading an IoT network this way is possible in " +
        "TestProject.io"
)
public class LoadAndRunSavedClient
    extends ActionWithSSLSocket implements WebAction
{
    @Parameter(description = "Network UUID")
    public String networkId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        new Controller(
            socketUrl,
            port,
            networkId
        ).execute();
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final DataStore store;
        private final String networkId;
        private final Connection connection;

        public Controller(String socketUrl, String port, String networkId)
            throws FailureException
        {
            this(
                new FileSystemJsonDataStore("./saved_instance/"),
                networkId,
                createSSLConnection(socketUrl, port, networkId)
            );
        }

        public Controller(
            DataStore store,
            String networkId,
            Connection connection
        ) {
            this.store = store;
            this.networkId = networkId;
            this.connection = connection;
        }

        private static SSLConnection createSSLConnection(
            String socketUrl,
            String port,
            String networkId
        )
            throws FailureException
        {
            try {
                return new SSLConnection(
                    socketUrl,
                    Integer.parseInt(port),
                    ((NetworkInstance) new FileSystemJsonDataStore("./saved_instance/")
                        .load(networkId, NetworkInstance.class)).certs
                );
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to establish socket connection: " + e.getMessage());
            }
        }

        public String execute() throws FailureException {
            try {
                RpcClient client = new RpcClient(connection);

                NetworkInstance instance = ((NetworkInstance)store
                    .load(networkId, NetworkInstance.class));

                VirtualIoTNetwork network = new VirtualIoTNetwork(
                    instance.schema, client
                );

                return instance.schema.meta.id.toString();
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to load file: " + e.getMessage()
                );
            }
        }
    }
}
