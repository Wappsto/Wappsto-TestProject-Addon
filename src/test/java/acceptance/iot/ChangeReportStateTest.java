package acceptance.iot;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.*;
import wappsto.api.network.model.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;
import wappsto.iot.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(UserInjector.class)
@ExtendWith(DataStoreInjector.class)
@ExtendWith(AdminInjector.class)
public class ChangeReportStateTest {

    @BeforeEach
    public void setup(Admin admin) throws Exception {
        resetUser(admin);
    }

    @Test
    public void changes_report_state(
        NetworkSchema schema,
        User user,
        DataStore store
    )
        throws Exception
    {
        NetworkService service = new RestNetworkService((RestUser) user);
        CreatorResponse creator = service.getCreator();
        schema.meta.id = UUID.fromString(creator.network.id);
        WappstoCerts certs = new WappstoCerts(creator);
        NetworkInstance instance = new NetworkInstance(certs, schema);
        VirtualIoTNetwork network = new VirtualIoTNetwork(
            schema,
            new RpcClient(
                new SSLConnection(
                    env().get(SOCKET_URL),
                    Integer.parseInt(env().get(SOCKET_PORT)),
                    certs
                )
            )
        );
        store.save(schema.meta.id.toString(), instance);
        service.claim(schema.meta.id.toString());
        logInBrowser(user.getId(), env().get(APP_URL));

        ChangeReportState action = new ChangeReportState();
        action.socketUrl = env().get(SOCKET_URL);
        action.port = env().get(SOCKET_PORT);
        action.networkId = creator.network.id;
        String reportState = schema.device.get(0).value.get(0).state.stream()
            .filter(s -> s.type.equals("Report"))
            .findAny()
            .orElseThrow().meta.id.toString();
        action.reportState = reportState;
        action.value = "1";
        runner().run(action);
        int attempts = 0;
        String value = service.getState(UUID.fromString(reportState));
        while (value.isEmpty() && attempts <= 10) {
            value = service.getState(UUID.fromString(reportState));
            attempts++;
            Thread.sleep(1000);
        }
        assertEquals(
            "1",
            service.getState(UUID.fromString(reportState))
        );
    }

    @AfterEach
    public void reset() throws Exception {
        resetRunner();
    }

    @AfterAll
    public static void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
