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

import java.util.*;

import static actions.Utils.*;

@Action(name = "Add number value to an RPC device")
public class AddNumberValueToDevice extends ActionWithSSLSocket implements WebAction {

    @Parameter(description = "Network UUID")
    public String networkId;

    @Parameter(description = "Device UUID")
    public String deviceId;

    @Parameter(description = "Type")
    public String type;

    @Parameter(description = "Value name")
    public String name;

    @Parameter(description = "Minimum value")
    public String min;

    @Parameter(description = "Maximum value")
    public String max;

    @Parameter(description = "Step size")
    public String stepSize;

    @Parameter(description = "Unit")
    public String unit;

    @Parameter(description = "Permissions (r, w, or rw")
    public String permissions;

    @Parameter(
        description = "Value UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String valueId;

    @Parameter(
        description = "Report state UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String reportId;

    @Parameter(
        description = "Control state UUID",
        direction = ParameterDirection.OUTPUT
    )
    public String controlId;

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        Controller controller = new Controller(
            networkId,
            deviceId,
            name,
            type,
            new NumberSchema(
                Float.parseFloat(min),
                Float.parseFloat(max),
                Float.parseFloat(stepSize),
                unit),
            permissions,
            socketUrl,
            port
        );
        ValueSchema value = controller.execute();
        valueId = value.meta.id.toString();
        StateSchema reportState = value.state.stream()
            .filter(s -> s.type.equals("Report"))
            .findAny()
            .orElse(null);
        reportId = (reportState != null)
            ? reportState.meta.id.toString()
            : "";
        StateSchema controlState = value.state.stream()
            .filter(s -> s.type.equals("Control"))
            .findAny()
            .orElse(null);
        controlId = (controlState != null)
            ? controlState.meta.id.toString()
            : "";

        return ExecutionResult.PASSED;

    }

    public static class Controller {
        private final String networkId;
        private final String deviceId;
        private final String name;
        private final String type;
        private final NumberSchema numbers;
        private final String permissions;
        private final DataStore store;
        private final Connection connection;

        public Controller(
            String networkId,
            String deviceId,
            String name,
            String type,
            NumberSchema numbers,
            String permissions,
            DataStore store,
            Connection connection
        ) {

            this.networkId = networkId;
            this.deviceId = deviceId;
            this.name = name;
            this.type = type;
            this.numbers = numbers;
            this.permissions = permissions;
            this.store = store;
            this.connection = connection;
        }

        public Controller(
            String networkId,
            String deviceId,
            String name,
            String type,
            NumberSchema numbers,
            String permissions,
            String socketUrl,
            String port
        )
            throws FailureException
        {
            this(
                networkId,
                deviceId,
                name,
                type,
                numbers,
                permissions,
                new FileSystemJsonDataStore(),
                createSSLConnection(networkId, socketUrl, port)
            );
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

            ValueSchema value;
            try {
                value = new ValueSchema(name, type, permissions, numbers);
            } catch (Exception e) {
                throw new FailureException("Invalid permission string");
            }

            NetworkSchema schema = instance.schema;
            schema.device.stream()
                .filter(d -> d.meta.id.equals(UUID.fromString(deviceId)))
                .findAny()
                .orElseThrow(() -> new FailureException(
                    "Device not found on network"
                )).value.add(value);

            instance.schema = schema;
            store.save(networkId, instance);
            try {
                new VirtualIoTNetwork(schema, new RpcClient(connection));
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to start network: " + e.getMessage()
                );
            }
            return value;
        }
    }
}
