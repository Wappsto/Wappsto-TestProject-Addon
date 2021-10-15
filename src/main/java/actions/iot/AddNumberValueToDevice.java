package actions.iot;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;

import java.util.*;

public class AddNumberValueToDevice extends ActionWithSSLSocket implements WebAction {

    @Parameter(description = "Network UUID")
    public String networkId;

    @Parameter(description = "Device UUID")
    public String deviceId;

    @Parameter(description = "Value name")
    public String name;

    @Parameter(description = "Type")
    public String type;

    @Parameter(description = "Minimum value")
    public String min;

    @Parameter(description = "Maximum value")
    public String max;

    @Parameter(description = "Step size")
    public String stepSize;

    @Parameter(description = "Permissions (r, w, or rw")
    public String permissions;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        return null;
    }

    public static class Controller {
        private String networkId;
        private String deviceId;
        private String name;
        private NumberSchema numbers;
        private String permissions;
        private DataStore store;
        private Connection connection;

        public Controller(
            String networkId,
            String deviceId,
            String name,
            NumberSchema numbers,
            String permissions,
            DataStore store,
            Connection connection
        ) {

            this.networkId = networkId;
            this.deviceId = deviceId;
            this.name = name;
            this.numbers = numbers;
            this.permissions = permissions;
            this.store = store;
            this.connection = connection;
        }

        public ValueSchema execute() throws FailureException {
            NetworkInstance instance;
            try {
                instance = ((NetworkInstance) store
                    .load(networkId, NetworkInstance.class));
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to load device from data store: " + e.getMessage()
                );
            }
            NetworkSchema schema = instance.schema;
            DeviceSchema device = schema.device.stream()
                .filter(d -> d.meta.id.equals(UUID.fromString(deviceId)))
                .findAny()
                .orElseThrow(() -> new FailureException(
                    "Device not found on network")
                );
            ValueSchema value;
            try {
                value = new ValueSchema(name, permissions, numbers);
            } catch (Exception e) {
                throw new FailureException("Invalid permission string");
            }
            device.value.add(value);
            instance.schema = schema;
            store.save(networkId, instance);
            try {
                new VirtualIoTNetwork(schema, new RpcClient(connection));
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to start network: " + e.getMessage()
                );
            }
            return null;
        }
    }
}
