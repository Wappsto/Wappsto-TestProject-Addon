package unit.actions.iot;

import actions.iot.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.network.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(VirtualIoTNetworkInjector.class)
public class StopClientControllerTest {
    @Test
    public void fails_to_stop_nonexistent_client() {
        assertThrows(
            FailureException.class,
            new StopClient.Controller(UUID.randomUUID().toString())::execute
        );
    }

    @Test
    public void stops_running_client(VirtualIoTNetwork network)
        throws FailureException
    {
        new StopClient.Controller(network.schema.meta.id.toString()).execute();
    }
}
