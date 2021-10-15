package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.enums.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;

public class AddDeviceToRpcClient
    extends ActionWithSSLSocket
    implements WebAction
{
    @Parameter(description = "Device name")
    public String name;
    @Parameter(
        description = "Device UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String deviceId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        return null;
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

        public String execute() throws FailureException {
            NetworkInstance instance;
            try {
                instance =
                    (NetworkInstance) store.load(
                        networkId,
                        NetworkInstance.class
                    );
            } catch (Exception e) {
                throw new FailureException(e.getMessage());
            }

            NetworkSchema schema = instance.schema;
            DeviceSchema device = new DeviceSchema(name);
            schema.device.add(device);
            instance.schema = schema;
            store.save(schema.meta.id.toString(), instance);
            try {
                VirtualIoTNetwork network = new VirtualIoTNetwork(
                    schema,
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
