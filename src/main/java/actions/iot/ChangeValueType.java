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

    @Override
    public ExecutionResult execute(WebAddonHelper helper) throws FailureException {
        Controller controller = new Controller(
            name,
            networkId,
            valueId,
            type,
            socketUrl,
            port
        );
        controller.execute();

        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final String name;
        private final String networkId;
        private final String valueId;
        private final String type;
        private final DataStore store;
        private final Connection connection;

        public Controller(
            String name,
            String networkId,
            String valueId,
            String type,
            DataStore store,
            Connection connection
        ) {
            this.name = name;
            this.networkId = networkId;
            this.valueId = valueId;
            this.type = type;
            this.store = store;
            this.connection = connection;
        }

        public Controller(
            String name,
            String networkId,
            String valueId,
            String type,
            String socketUrl,
            String port
        ) throws FailureException {

            this(
                name,
                networkId,
                valueId,
                type,
                new FileSystemJsonDataStore(),
                createSSLConnection(networkId, socketUrl, port)
            );
        }

        public void execute() throws FailureException {
            NetworkInstance instance;
            try {
                instance = (NetworkInstance) store
                    .load(networkId, NetworkInstance.class);
            } catch (Exception e) {
                throw new FailureException(
                    "Failed to load network: " + e.getMessage()
                );
            }

            Optional<ValueSchema> value = findValue(instance);

            ValueSchema valueSchema;
            if (value.isPresent()) {
                valueSchema = value.get();
            } else {
                throw new FailureException("Invalid value ID");
            }

            valueSchema.name = name.isEmpty()
                ? valueSchema.name
                : name;

            valueSchema.type = type.isEmpty()
                ? valueSchema.type
                : type;

            updateValue(instance, valueSchema);
            VirtualIoTNetwork network;
            try {
                network = new VirtualIoTNetwork(
                    instance.schema, new RpcClient(connection)
                );
            } catch (Exception e) {
                throw new FailureException("Failed to start client");
            }
            instance.schema = network.schema;
            store.save(networkId, instance);
        }

        private void updateValue(NetworkInstance instance, ValueSchema valueSchema) {
            instance.schema.device.stream().filter(
                d -> d.value.stream().anyMatch(
                    v -> v.meta.id.toString().equals(valueId)
                )
            ).findAny()
                .get()
                .value.replaceAll(
                    v -> v.meta.id.toString().equals(valueId)
                        ? valueSchema
                        : v
                );
        }

        private Optional<ValueSchema> findValue(NetworkInstance instance) {
            return instance.schema.device.stream()
                .flatMap( d -> d.value.stream())
                .filter( v -> v.meta.id.toString().equals(valueId))
                .findAny();
        }
    }
}
