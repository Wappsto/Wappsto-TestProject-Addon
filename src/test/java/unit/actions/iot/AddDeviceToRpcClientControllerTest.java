package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.*;

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
            .Controller(networkId, store, connection);

        assertThrows(
            FailureException.class,
            controller::execute
        );
    }

    @Test
    @Tag("connection-refused")
    public void fails_to_start_if_connection_is_refused(
        DataStore store,
        Connection connection
    ) {

    }
}
