package integration.iot;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.session.*;
import wappsto.iot.network.*;
import wappsto.iot.rpc.model.*;

import java.util.*;

import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(VirtualIoTNetworkInjector.class)
@ExtendWith(NetworkServiceInjector.class)
public class VirtualIoTNetworkIntegrationTest {

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        resetUser(admin);
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

    @Test
    public void sends_state_change_to_dashboard(
        VirtualIoTNetwork network,
        NetworkService service
    )
        throws Exception
    {
        service.claim(network.schema.meta.id.toString());
        network.updateControlState(
            new StateData(
                network.schema.device.get(0).value.get(0).state.stream()
                    .filter(s -> s.type.equals("Control"))
                    .findAny()
                    .orElseThrow().meta.id,
                "1"
            )
        );
        assertEventuallyEquals(
            "1",
            id -> service.getState((UUID) id),
            network.schema.device.get(0).value.get(0).state.stream()
                .filter(s -> s.type.equals("Report"))
                .findAny()
                .orElseThrow().meta.id
        );
        network.stop();
    }

    @Test
    public void retrieves_data_from_dashboard(
        VirtualIoTNetwork network,
        NetworkService service
    )
        throws Exception
    {
        service.claim(network.schema.meta.id.toString());
        UUID controlState = network.schema.device.get(0).value.get(0).state
            .stream()
            .filter(s -> s.type.equals("Control"))
            .findAny()
            .orElseThrow().meta.id;

        UUID reportState = network.schema.device.get(0).value.get(0).state
            .stream()
            .filter(s -> s.type.equals("Report"))
            .findAny()
            .orElseThrow().meta.id;
        waitForResponse(
            request -> updateState((UpdateStateRequest) request),
            new UpdateStateRequest(service, controlState, "1")
        );
        assertEventuallyEquals(
            "1",
            id -> service.getState((UUID) id),
            reportState
        );
        network.stop();
    }

    private boolean updateState(UpdateStateRequest request) throws Exception {
        request.service.updateState(request.state, request.data);
        return true;
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }

    private class UpdateStateRequest {
        public final NetworkService service;
        public final UUID state;
        public final String data;
        public UpdateStateRequest(NetworkService service, UUID state, String data) {
            this.service = service;
            this.state = state;
            this.data = data;
        }

    }
}
