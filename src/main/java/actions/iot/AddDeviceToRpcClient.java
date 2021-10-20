package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.filesystem.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;

@Action(name = "Add empty device to RPC client")
public class AddDeviceToRpcClient
    extends ActionWithSSLSocket
    implements WebAction
{
    @Parameter(description = "Network UUID")
    public String networkId;

    @Parameter(description = "Device name")
    public String name;
    @Parameter(
        description = "Device UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String deviceId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {

        Controller controller = new Controller(
            name,
            networkId,
            socketUrl,
            port
        );
        deviceId = controller.execute();
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final String networkId;
        private final String name;
        private final DataStore store;
        private final Connection connection;

        public Controller(
            String name,
            String networkId,
            DataStore store,
            Connection connection
        ) {

            this.name = name;
            this.networkId = networkId;
            this.store = store;
            this.connection = connection;
        }

        public Controller(
            String name,
            String networkId,
            String socketUrl,
            String port
        ) throws FailureException {
            this(
                name,
                networkId,
                new FileSystemJsonDataStore(),
                createSSLConnection(networkId, socketUrl, port)
            );
        }

        private static SSLConnection createSSLConnection(
            String networkId,
            String socketUrl,
            String port
        )
            throws FailureException
        {
            try {
                return new SSLConnection(
                    socketUrl,
                    Integer.parseInt(port),
                    getCerts(networkId)
                );
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to create SSL Socket: " + e.getMessage()
                );
            }
        }

        private static WappstoCerts getCerts(String networkId)
            throws FailureException
        {
            try {
                return ((NetworkInstance)new FileSystemJsonDataStore()
                    .load(networkId, NetworkInstance.class)).certs;
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to load network from datastore"
                );
            }
        }

        public String execute() throws FailureException {
            NetworkInstance instance;
            try {
                instance = (NetworkInstance) store.load(
                    networkId,
                    NetworkInstance.class
                );
            } catch (Exception e) {
                throw new FailureException(e.getMessage());
            }
            DeviceSchema device = new DeviceSchema(name);
            instance.schema.device.add(device);
            store.save(instance.schema.meta.id.toString(), instance);
            try {
                new VirtualIoTNetwork(
                    instance.schema,
                    new RpcClient(connection)
                );
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to start network: " + e.getMessage()
                );
            }
            return device.meta.id.toString();
        }
    }
}
