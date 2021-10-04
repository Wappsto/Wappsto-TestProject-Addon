package unit.actions.network;

import actions.network.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.network.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(NetworkServiceInjector.class)
public class ClaimNetworkControllerTest {
    @Test
    public void claims_network(NetworkService service) throws Exception {
        UUID network = UUID.fromString(service.create().id);
        ClaimNetwork.Controller controller = new ClaimNetwork
            .Controller(service, network.toString());
        assertDoesNotThrow(() -> controller.execute());
    }

    @Test
    public void fails_to_claim_nonexistent_network(NetworkService service) {
        UUID network = UUID.randomUUID();
        ClaimNetwork.Controller controller = new ClaimNetwork
            .Controller(service, network.toString());
        assertThrows(FailureException.class, () -> controller.execute());
    }
}
