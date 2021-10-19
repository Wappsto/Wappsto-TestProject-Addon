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
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(ConnectionInjector.class)
@ExtendWith(DataStoreInjector.class)
public class ChangeValueTypeControllerTest {
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
    public void fails_to_load_nonexistent_network(Connection connection) {
        String networkId = UUID.randomUUID().toString();
        ChangeValueType.Controller controller =
            new ChangeValueType.Controller(
                "Test", networkId,
                schema.device.get(0).value.get(0).meta.id.toString(),
                "Number",
                new NumberSchema(0, 1, 1, "Change value type test"),
                ValuePermission.RW,
                store,
                connection
            );
        Exception error = assertThrows(
            FailureException.class,
            controller::execute
        );
        assertTrue(error.getMessage().contains("Failed to load network"));
    }

    @Test
    public void fails_to_change_nonexistent_value(Connection connection) {
        String networkId = schema.meta.id.toString();
        String valueId = UUID.randomUUID().toString();
        ChangeValueType.Controller controller =
            new ChangeValueType.Controller(
                "Test", networkId,
                valueId,
                "Number",
                new NumberSchema(0, 1, 1, "Change value type test"),
                ValuePermission.RW,
                store,
                connection
            );
        Exception error = assertThrows(
            FailureException.class,
            controller::execute
        );
        assertTrue(error.getMessage().contains("Invalid value ID"));
    }

    @Test
    public void updates_value(Connection connection) throws FailureException {
        String networkId = schema.meta.id.toString();
        String valueId = schema.device.get(0).value.get(0).meta.id.toString();
        ChangeValueType.Controller controller =
            new ChangeValueType.Controller(
                "Test", networkId,
                valueId,
                "Number",
                new NumberSchema(0, 1, 1, "Change value type test"),
                ValuePermission.RW,
                store,
                connection
            );
        controller.execute();
        assertTrue(((InMemoryConnection)connection).wasReceived(valueId));
    }
}
