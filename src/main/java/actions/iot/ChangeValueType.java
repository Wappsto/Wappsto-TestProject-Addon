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

@Action(name = "Change value type of existing value on RPC client")
public class ChangeValueType extends ActionWithSSLSocket implements WebAction {
    @Parameter(description = "Network UUID")
    public String networkId;

    @Parameter(description = "Value UUID to change")
    public String valueId;

    @Parameter(description = "Name")
    public String name;

    @Parameter(description = "Type")
    public String type;

    @Parameter(description = "Minimum")
    public String min;

    @Parameter(description = "Maximum")
    public String max;

    @Parameter(description = "Step size")
    public String stepSize;

    @Parameter(description = "Unit")
    public String unit;

    @Parameter(description = "Permissions (r/w/rw")
    public String permissions;

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

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        Controller controller = new Controller(
            name,
            networkId,
            valueId,
            type,
            new NumberSchema(
                Float.parseFloat(min),
                Float.parseFloat(max),
                Float.parseFloat(stepSize),
                unit
            ),
            permissions,
            socketUrl,
            port
        );
        ValueSchema valueSchema = controller.execute();
        Optional<StateSchema> control = valueSchema.state.stream().filter(
            s -> s.type.equals("Control")
        ).findAny();

        Optional<StateSchema> report = valueSchema.state.stream().filter(
            s -> s.type.equals("Report")
        ).findAny();

        controlState = control.isPresent()
            ? control.get().meta.id.toString()
            : "";

        reportState = report.isPresent()
            ? report.get().meta.id.toString()
            : "";



        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final String name;
        private final String networkId;
        private final String valueId;
        private final String type;
        private final NumberSchema numbers;
        private final ValuePermission valuePermission;
        private final DataStore store;
        private final Connection connection;

        public Controller(
            String name,
            String networkId,
            String valueId,
            String type,
            NumberSchema numbers,
            ValuePermission valuePermission,
            DataStore store,
            Connection connection
        ) {
            this.name = name;
            this.networkId = networkId;
            this.valueId = valueId;
            this.type = type;
            this.numbers = numbers;
            this.valuePermission = valuePermission;
            this.store = store;
            this.connection = connection;
        }

        public Controller(
            String name,
            String networkId,
            String valueId,
            String type,
            NumberSchema numbers,
            String permissions,
            String socketUrl,
            String port
        ) throws FailureException {

            this(
                name,
                networkId,
                valueId,
                type,
                numbers,
                getValuePermissions(permissions),
                new FileSystemJsonDataStore(),
                createSSLConnection(networkId, socketUrl, port)
            );
        }


        private static ValuePermission getValuePermissions(String permissions)
            throws FailureException
        {
            try {
                return ValuePermission.from(permissions);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to instantiate controller: " + e.getMessage()
                );
            }
        }

        public ValueSchema execute() throws FailureException {
            NetworkInstance instance;
            try {
                instance = (NetworkInstance) store
                    .load(networkId, NetworkInstance.class);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to load network: " + e.getMessage()
                );
            }

            ValueSchema valueSchema = new ValueSchema(
                name,
                type,
                valuePermission,
                numbers
            );
            valueSchema.meta.id = UUID.fromString(valueId);

            instance.schema.device.stream().filter(
                d -> d.value.stream().anyMatch(
                    v -> v.meta.id.toString().equals(valueId)
                )
            ).findAny()
                .orElseThrow(() -> new FailureException("Invalid value ID"))
                .value.replaceAll(
                    v -> v.meta.id.toString().equals(valueId)
                        ? valueSchema
                        : v
                );
            store.save(networkId, instance);
            try {
                new VirtualIoTNetwork(
                    instance.schema, new RpcClient(connection)
                );
            } catch (Exception e) {
                throw new FailureException("Failed to start client");
            }
            return valueSchema;
        }
    }
}
