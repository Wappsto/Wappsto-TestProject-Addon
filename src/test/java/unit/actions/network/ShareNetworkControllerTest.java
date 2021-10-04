package unit.actions.network;

import actions.network.*;
import extensions.injectors.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.network.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
@ExtendWith(NetworkServiceInjector.class)
public class ShareNetworkControllerTest {
    @Test
    public void fails_to_share_nonexistent_network(NetworkService service) {
        String network = UUID.randomUUID().toString();
        ShareNetwork.Controller controller = new ShareNetwork.Controller(
            service,
            network,
            "other"
        );
        assertThrows(FailureException.class, () -> controller.execute());
    }

    @Test
    public void shares_network(NetworkService service) throws Exception {
        String network = service.create().id;
        ShareNetwork.Controller controller = new ShareNetwork.Controller(
            service,
            network,
            "other"
        );
        assertDoesNotThrow(() -> controller.execute());
    }
}
