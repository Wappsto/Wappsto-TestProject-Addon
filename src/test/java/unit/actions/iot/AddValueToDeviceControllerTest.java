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
@ExtendWith(ConnectionInjector.class)
@ExtendWith(NetworkSchemaInjector.class)
public class AddValueToDeviceControllerTest {
    private DataStore store;
    private NetworkSchema schema;

    @BeforeEach
    public void setup(DataStore store, NetworkSchema schema) {
        this.store = store;
        this.schema = schema;
        store.save(schema.meta.id.toString(), new NetworkInstance(
            new WappstoCerts(), schema
        ));
    }

    @Test
    public void fails_to_add_value_to_nonexistent_device(
        Connection connection
    ) {
        String deviceId = UUID.randomUUID().toString();
        AddNumberValueToDevice.Controller controller =
            new AddNumberValueToDevice.Controller(
                schema.meta.id.toString(),
                deviceId,
                "Add value test",
                new NumberSchema(0, 1, 1, "Boolean"),
                "rw",
                store,
                connection
            );
        assertTrue(
            assertThrows(FailureException.class, controller::execute)
                .getMessage().contains("Device not found")
        );
    }

    @Test
    public void fails_to_add_value_with_invalid_permission(
        Connection connection
    ) {
        String deviceId = schema.device.get(0).meta.id.toString();
        AddNumberValueToDevice.Controller controller =
            new AddNumberValueToDevice.Controller(
                schema.meta.id.toString(),
                deviceId,
                "Add value test",
                new NumberSchema(0, 1, 1, "Boolean"),
                "invalid",
                store,
                connection
            );
        assertTrue(
            assertThrows(FailureException.class, controller::execute)
                .getMessage().contains("Invalid permission string")
        );
    }

    @Test
    public void saves_value_to_data_store(Connection connection)
        throws Exception
    {
        AddNumberValueToDevice.Controller controller =
            createController(connection);
        controller.execute();
        NetworkInstance loadedInstance = (NetworkInstance)store.load(
            schema.meta.id.toString(),
            NetworkInstance.class
        );
        assertTrue(loadedInstance.schema.device.get(0).value.stream()
            .anyMatch(v -> v.name.equals("Add value test"))
        );
    }

    @Test
    public void publishes_value_to_server(Connection connection)
        throws FailureException
    {
        AddNumberValueToDevice.Controller controller =
            createController(connection);
        controller.execute();
        assertTrue(((InMemoryConnection)connection).wasReceived(
            "Add value test"
        ));
    }

    private AddNumberValueToDevice.Controller createController(
        Connection connection
    ) {
        String deviceId = schema.device.get(0).meta.id.toString();
        return new AddNumberValueToDevice.Controller(
            schema.meta.id.toString(),
            deviceId,
            "Add value test",
            new NumberSchema(0, 1, 1, "Boolean"),
            "rw",
            store,
            connection
        );
    }
}
