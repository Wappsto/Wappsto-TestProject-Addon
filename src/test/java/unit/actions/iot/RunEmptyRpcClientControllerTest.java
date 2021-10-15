package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import extensions.mocks.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(DataStoreInjector.class)
@ExtendWith(ConnectionInjector.class)
@ExtendWith(CreatorResponseInjector.class)
public class RunEmptyRpcClientControllerTest {
    @Test
    public void creates_virtual_iot_network(
        Connection connection,
        CreatorResponse creator,
        DataStore store
    ) throws FailureException {
        RunEmptyRpcClient.Controller controller = new RunEmptyRpcClient.Controller(
            "Test",
            creator,
            connection,
            store
        );
        controller.execute();
        assertTrue(
            ((InMemoryConnection) connection).wasReceived(creator.network.id)
        );
    }
}
