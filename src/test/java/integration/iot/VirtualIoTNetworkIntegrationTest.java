package integration.iot;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.network.*;
import wappsto.iot.rpc.model.*;
import wappsto.network.*;
import wappsto.rest.request.exceptions.*;
import wappsto.session.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

        network.update(
            new ControlStateData(
                network.schema.device.get(0).value.get(0).state.stream()
                    .filter(s -> s.type.equals("Control"))
                    .findAny()
                    .orElseThrow().meta.id,
                "1"
            )
        );
        Thread.sleep(2000);
        assertEquals(
            "1",
            service.getState(
                network.schema.device.get(0).value.get(0).state.stream()
                    .filter(s -> s.type.equals("Report"))
                    .findAny()
                    .orElseThrow().meta.id
            )
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
        Thread.sleep(500);
        service.updateState(controlState, "1");
        Thread.sleep(500);
        assertEquals("1", service.getState(reportState));
        network.stop();
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }
}
