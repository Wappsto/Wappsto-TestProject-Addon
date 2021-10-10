package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.model.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(DataStoreInjector.class)
@ExtendWith(ConnectionInjector.class)
@ExtendWith(CreatorResponseInjector.class)
public class RunSimpleRpcClientControllerTest {
    @Test
    public void creates_virtual_iot_network(
        Connection connection,
        CreatorResponse creator,
        DataStore store
    ) {
        RunSimpleRPCClient.Controller controller = new RunSimpleRPCClient
            .Controller(
                "Test",
                creator,
                new NumberSchema(0, 1, 1, "Boolean"),
                connection,
                store
            );
        VirtualIoTNetwork network = controller.execute();
        assertEquals(
            creator.network.id,
            network.schema.meta.id.toString()
        );
    }

    @Test
    public void saves_network_instance_to_data_persistence(
        DataStore store,
        Connection connection,
        CreatorResponse creator
    )
        throws Exception
    {
        RunSimpleRPCClient.Controller controller = new RunSimpleRPCClient
            .Controller(
            "Test",
            creator,
            new NumberSchema(0, 1, 1, "Boolean"),
            connection,
            store
        );
        String expected = creator.network.id;
        NetworkInstance retrievedInstance = (NetworkInstance) store.load(
            expected,
            NetworkInstance.class
        );
        String actual = retrievedInstance.schema.meta.id.toString();
        assertEquals(expected, actual);
    }
}
