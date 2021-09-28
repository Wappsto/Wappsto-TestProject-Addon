package iot;

import org.junit.jupiter.api.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;
import wappsto.rest.network.*;
import wappsto.rest.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import java.util.*;

import static iot.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

public class VirtualIoTNetworkIntegrationTest {
    private static String serviceUrl;
    private User session;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        resetUser();
    }

    private static void resetUser() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

    @BeforeEach
    public void newUser() throws Exception {
        session = createNewUserSession(serviceUrl);
    }

    @Test
    public void sends_state_change_to_dashboard() throws Exception {
        //TODO: There's an object or three missing in here
        NetworkService service = new NetworkService(session);
        CreatorResponse creator = service.getCreator();

        WappstoCerts certs = new WappstoCerts(creator);

        SSLConnection connection = new SSLConnection(
            "qa.wappsto.com",
            53005,
            certs
        );
        RPCClient client = new RPCClient(connection);
        NetworkSchema schema = defaultNetwork(creator);
        VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);

        network.update(
            new ControlStateData(
                "1",
                schema.device.get(0).value.get(0).state.stream()
                    .filter(s -> s.type == "Control")
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
                    .filter(s -> s.type == "Report")
                    .findAny()
                    .orElseThrow().meta.id
            )
        );
        client.stop();
    }

    @Test
    public void retrieves_data_from_dashboard() throws Exception {
        NetworkService service = new NetworkService(session);
        CreatorResponse creator = service.getCreator();

        WappstoCerts certs = new WappstoCerts(creator);

        SSLConnection connection = new SSLConnection(
            "qa.wappsto.com",
            53005,
            certs
        );
        RPCClient client = new RPCClient(connection);
        NetworkSchema schema = defaultNetwork(creator);
        VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);

        UUID controlState = schema.device.get(0).value.get(0).state.stream()
            .filter(s -> s.type.equals("Control"))
            .findAny()
            .orElseThrow().meta.id;

        UUID reportState = schema.device.get(0).value.get(0).state.stream()
            .filter(s -> s.type.equals("Report"))
            .findAny()
            .orElseThrow().meta.id;

        service.updateState(controlState, "1");
        Thread.sleep(2000);
        assertEquals("1", service.getState(reportState));
        client.stop();
    }

    @AfterEach
    public void tearDown() throws Exception {
        resetUser();
    }
}
