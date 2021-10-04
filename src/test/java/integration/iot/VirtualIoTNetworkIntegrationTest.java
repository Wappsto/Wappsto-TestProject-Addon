package integration.iot;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.rest.network.*;
import wappsto.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;
import wappsto.session.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(AdminInjector.class)
public class VirtualIoTNetworkIntegrationTest {
    private static String serviceUrl;
    private RestUser session;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        resetUser(admin);
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

    @BeforeEach
    public void newUser(Admin admin) throws Exception {
        session = createNewUserSession(serviceUrl, admin);
    }

    @Test
    public void sends_state_change_to_dashboard(NetworkSchema schema) throws Exception {
        //TODO: There's an object or three missing in here
        RestNetworkService service = new RestNetworkService(session);
        CreatorResponse creator = service.getCreator();

        RPCClient client = createClient(creator);
        schema.meta.id = UUID.fromString(creator.network.id);
        VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);

        network.update(
            new ControlStateData(
                schema.device.get(0).value.get(0).state.stream()
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
                schema.device.get(0).value.get(0).state.stream()
                    .filter(s -> s.type.equals("Report"))
                    .findAny()
                    .orElseThrow().meta.id
            )
        );
        client.stop();
    }

    @Test
    public void retrieves_data_from_dashboard(NetworkSchema schema) throws Exception {
        VirtualIoTNetwork network = createVirtualIoTClient(session, schema);
        RestNetworkService service = new RestNetworkService(session);

        UUID controlState = schema.device.get(0).value.get(0).state.stream()
            .filter(s -> s.type.equals("Control"))
            .findAny()
            .orElseThrow().meta.id;

        UUID reportState = schema.device.get(0).value.get(0).state.stream()
            .filter(s -> s.type.equals("Report"))
            .findAny()
            .orElseThrow().meta.id;
        Thread.sleep(500);
        service.updateState(controlState, "1");
        Thread.sleep(500);
        assertEquals("1", service.getState(reportState));
        network.client.stop();
    }

    private VirtualIoTNetwork createVirtualIoTClient(
        RestUser session,
        NetworkSchema schema
    )
        throws Exception
    {
        RestNetworkService service = new RestNetworkService(session);
        CreatorResponse creator = service.getCreator();

        RPCClient client = createClient(creator);
        schema.meta.id = UUID.fromString(creator.network.id);
        return new VirtualIoTNetwork(schema, client);
    }

    private RPCClient createClient(CreatorResponse creator)
        throws Exception
    {
        return new RPCClient.Builder(creator)
            .connectingTo(
                env().get(SOCKET_URL),
                Integer.parseInt(env().get(SOCKET_PORT))
            ).build();
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }
}
