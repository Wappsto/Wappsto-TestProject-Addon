package actions.iot;

import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.rpc.*;
import wappsto.network.model.*;

public class LoadAndRunSavedClient implements GenericAction {
    @Override
    public ExecutionResult execute(AddonHelper helper) throws FailureException {
        return null;
    }

    public static class Controller {
        private final DataStore store;
        private final String networkId;
        private final Connection connection;

        public Controller(
            DataStore store,
            String networkId,
            Connection connection
        ) {
            this.store = store;
            this.networkId = networkId;
            this.connection = connection;
        }

        public String execute() throws FailureException {
            try {
                RPCClient client = new RPCClient(connection);

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
