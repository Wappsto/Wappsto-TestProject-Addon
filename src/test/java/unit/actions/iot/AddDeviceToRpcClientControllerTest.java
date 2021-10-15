package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import extensions.mocks.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.network.model.*;
import wappsto.iot.ssl.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(DataStoreInjector.class)
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(ConnectionInjector.class)
public class AddDeviceToRpcClientControllerTest {
    @Test
    public void fails_to_load_nonexistent_client(
        DataStore store,
        Connection connection
    ) {
        String networkId = UUID.randomUUID().toString();
        AddDeviceToRpcClient.Controller controller = new AddDeviceToRpcClient
            .Controller("Test", networkId, store, connection);

        assertTrue(
            assertThrows(
                FailureException.class,
                controller::execute
            ).getMessage().contains("Failed to load")
        );
    }

    @Test
    @Tag("connection-refused")
    public void fails_to_start_if_connection_is_refused(
        DataStore store,
        Connection connection,
        NetworkSchema schema
    ) {
        saveNetworkInstanceTo(store, schema);
        AddDeviceToRpcClient.Controller controller = new AddDeviceToRpcClient
            .Controller("Test", schema.meta.id.toString(), store, connection);
        assertTrue(
            assertThrows(
                FailureException.class,
                controller::execute
            ).getMessage().contains("Connection refused")
        );
    }

    @Test
    public void publishes_new_device_to_server(
        DataStore store,
        Connection connection,
        NetworkSchema schema
    )
        throws FailureException
    {
        saveNetworkInstanceTo(store, schema);
        AddDeviceToRpcClient.Controller controller = new AddDeviceToRpcClient
            .Controller("Test", schema.meta.id.toString(), store, connection);
        String deviceId = controller.execute();
        assertTrue(((InMemoryConnection)connection).wasReceived(deviceId));
    }

    @Test
    public void saves_new_device_to_data_store(
        DataStore store,
        Connection connection,
        NetworkSchema schema
    )
        throws Exception
    {
        saveNetworkInstanceTo(store, schema);
        AddDeviceToRpcClient.Controller controller = new AddDeviceToRpcClient
            .Controller("Test", schema.meta.id.toString(), store, connection);
        String deviceId = controller.execute();
        NetworkInstance loadedInstance = (NetworkInstance) store
            .load(schema.meta.id.toString(), NetworkInstance.class);
        assertTrue(loadedInstance.schema.device.stream()
            .anyMatch(d -> d.meta.id.toString().equals(deviceId))
        );
    }

    private void saveNetworkInstanceTo(DataStore store, NetworkSchema schema) {
        store.save(
            schema.meta.id.toString(),
            new NetworkInstance(new WappstoCerts(), schema)
        );
    }
}
