package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.filesystem.*;
import wappsto.iot.network.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.ssl.*;

import java.util.*;

@Action(name = "Change report state")
public class ChangeReportState extends ActionWithSSLSocket implements WebAction {
    @Parameter(description = "Network UUID")
    public String networkId;
    @Parameter(description = "State UUID")
    public String reportState;
    @Parameter(description = "Value")
    public String value;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        new Controller(
            networkId,
            reportState,
            value,
            socketUrl,
            port
        ).execute();
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final String networkId;
        private final DataStore store;
        private final Connection connection;
        private final String value;
        private String state;

        public Controller(
            String networkId,
            String reportState,
            String value,
            String socketUrl,
            String port
        )
            throws FailureException
        {
            this(
                networkId,
                getFileSystemStore(),
                createConnection(socketUrl, port, networkId),
                reportState,
                value
            );
        }

        private static FileSystemJsonDataStore getFileSystemStore() {
            return new FileSystemJsonDataStore("./saved_instance/");
        }

        private static SSLConnection createConnection(
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
                    ((NetworkInstance) getFileSystemStore()
                        .load(networkId, NetworkInstance.class)).certs
                );
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to establish SSL connection" + e.getMessage()
                );
            }
        }

        public Controller(
            String networkId,
            DataStore store,
            Connection connection,
            String state,
            String value
        ) {

            this.networkId = networkId;
            this.store = store;
            this.connection = connection;
            this.state = state;
            this.value = value;
        }

        public void execute() throws FailureException {
            try {
                NetworkInstance instance = (NetworkInstance) store.load(
                    networkId,
                    NetworkInstance.class
                );

                RpcClient client = new RpcClient(connection);
                VirtualIoTNetwork network = new VirtualIoTNetwork(
                    instance.schema,
                    client
                );
                network.updateReportState(
                    new StateData(UUID.fromString(state), value)
                );
                instance.schema = network.schema;
                store.save(networkId, network.schema);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to load network from data store: " + e.getMessage());
            }

        }
    }
}
