package acceptance;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.*;
import wappsto.iot.network.*;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utils.*;

@Disabled
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(VirtualIoTNetworkInjector.class)
public class StopClientTest {

    @Test
    public void fails_to_stop_nonexistent_client(
        VirtualIoTNetwork network
    ) {
        VirtualIoTNetworkStore.getInstance().addNetwork(network);

        StopClient action = new StopClient();
        action.network = network.schema.meta.id.toString();

        assertThrows(
            ExecutionException.class,
            () -> runner().run(action)
        );
    }
}
