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

    @Test
    public void reports_its_state_on_startup(NetworkSchema schema, Connection connection) {
        startNetwork(schema, connection);
        assertTrue(
            wasReceived(
                schema.meta.id.toString(),
                (InMemoryConnection) connection
            )
        );
    }

    @Test
    public void updates_report_state_on_control_state_change(
        NetworkSchema schema,
        Connection connection
    ) {
        VirtualIoTNetwork network = startNetwork(schema, connection);
        UUID state = network.getControlStateId(0);

        StateData request = new StateData(state, "1");
        network.updateControlState(request);
        assertTrue(
            wasReceived("\"data\":\"1\"", (InMemoryConnection)connection)
        );
    }

    @Test
    public void can_change_its_report_state_directly(
        NetworkSchema schema,
        Connection connection
    )
        throws Exception
    {
        VirtualIoTNetwork network = startNetwork(schema, connection);
        UUID state = network.getReportStateId(0);
        network.updateReportState(new StateData(state, "1"));
        assertTrue(
            wasReceived("\"data\":\"1\"", (InMemoryConnection)connection)
        );
    }

    @Test
    public void fails_to_update_invalid_report_state(
        NetworkSchema schema,
        Connection connection
    ) {
        VirtualIoTNetwork network = startNetwork(schema, connection);
        UUID state = UUID.randomUUID();
        assertThrows(
            Exception.class,
            () -> network.updateReportState(new StateData(state, "1"))
        );
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
