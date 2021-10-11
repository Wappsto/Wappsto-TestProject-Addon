package unit.actions.network;

import actions.network.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(NetworkServiceInjector.class)
public class CreateDeviceUnderNetworkControllerTest {
    @Test
    public void fails_to_create_under_nonexistent_network(
        NetworkService service
    ) {
        String id = UUID.randomUUID().toString();
        CreateDeviceUnderNetwork.Controller controller =
            new CreateDeviceUnderNetwork.Controller(service, id);
        assertThrows(FailureException.class, controller::execute);
    }

    @Test
    public void creates_device_under_existing_network(
        NetworkService service
    )
        throws Exception
    {
        String id = service.createNetwork().id;
        CreateDeviceUnderNetwork.Controller controller =
            new CreateDeviceUnderNetwork.Controller(service, id);
        String deviceId = controller.execute();
        assertNotNull(deviceId);
    }
}
