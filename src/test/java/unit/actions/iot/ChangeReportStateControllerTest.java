package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import extensions.mocks.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(ConnectionInjector.class)
@ExtendWith(DataStoreInjector.class)
public class ChangeReportStateControllerTest {
    @Test
    public void updates_report_state(
        NetworkSchema schema,
        Connection connection,
        DataStore store
    )
        throws FailureException
    {
        VirtualIoTNetwork network = startNetwork(schema, connection, store);
        ChangeReportState.Controller controller =
            new ChangeReportState.Controller(
                schema.meta.id.toString(),
                store,
                connection,
                network.getReportStateId(0).toString(),
                "1"
            );
        controller.execute();
        assertTrue(
            wasReceived("\"data\":\"1\"", ((InMemoryConnection) connection))
        );
    }

    @Test
    public void fails_to_update_invalid_state(
        NetworkSchema schema,
        Connection connection,
        DataStore store
    ) {
        VirtualIoTNetwork network = startNetwork(schema, connection, store);
        ChangeReportState.Controller controller =
            new ChangeReportState.Controller(
                schema.meta.id.toString(),
                store,
                connection,
                UUID.randomUUID().toString(),
                "1"
            );
        assertThrows(FailureException.class, controller::execute);
    }

    private VirtualIoTNetwork startNetwork(
        NetworkSchema schema,
        Connection connection,
        DataStore store
    ) {
        RpcClient client = new RpcClient(connection);
        VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);
        store.save(
            schema.meta.id.toString(),
            new NetworkInstance(new WappstoCerts(), schema)
        );
        return network;
    }

    private boolean wasReceived(String message, InMemoryConnection connection) {
        return connection.lastReceived.contains(message);
    }
}
