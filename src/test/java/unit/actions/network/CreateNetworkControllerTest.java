package unit.actions.network;

import actions.network.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(NetworkServiceInjector.class)
public class CreateNetworkControllerTest {
    @Test
    public void creates_network(NetworkService service) throws Exception {
        CreateNetwork.Controller controller = new CreateNetwork
            .Controller(service);
        assertNotNull(controller.execute());
    }
}
