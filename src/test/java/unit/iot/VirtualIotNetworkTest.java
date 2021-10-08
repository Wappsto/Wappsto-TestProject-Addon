package unit.iot;

import extensions.injectors.*;
import extensions.mocks.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(ConnectionInjector.class)
public class VirtualIotNetworkTest {

    @Nested
    public class reports_its_state {
        @Test
        public void on_startup(NetworkSchema schema, Connection connection) {
            startNetwork(schema, connection);
            assertTrue(
                wasReceived(
                    schema.meta.id.toString(),
                    (InMemoryConnection) connection
                )
            );
        }

        @Test
        public void on_state_change(
            NetworkSchema schema,
            Connection connection
        ) {
            VirtualIoTNetwork network = startNetwork(schema, connection);
            UUID state = schema.device.get(0)
                .value.get(0)
                .state.get(1)
                .meta.id;


            ControlStateData request = new ControlStateData(state, "1");
            network.update(request);
            assertTrue(
                wasReceived("\"data\":\"1\"", (InMemoryConnection)connection));
        }
    }

    @Test
    public void stops(NetworkSchema schema, Connection connection) {
        VirtualIoTNetwork network = startNetwork(schema, connection);
        network.stop();
        assertFalse(network.isRunning());
        assertFalse(((InMemoryConnection)connection).isConnected);
    }

    private VirtualIoTNetwork startNetwork(
        NetworkSchema schema,
        Connection connection
    ) {
        RpcClient client = new RpcClient(connection);
        return new VirtualIoTNetwork(schema, client);
    }

    private boolean wasReceived(String message, InMemoryConnection connection) {
        return connection.lastReceived.contains(message);
    }
}
