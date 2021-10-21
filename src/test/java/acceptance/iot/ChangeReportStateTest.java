package acceptance.iot;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
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

import static util.Env.*;
import static util.Utils.*;

@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(UserInjector.class)
@ExtendWith(DataStoreInjector.class)
@ExtendWith(AdminInjector.class)
public class ChangeReportStateTest {

    private CreatorResponse creator;
    private NetworkSchema schema;
    private RestNetworkService service;

    @BeforeAll
    public static void cleanSlate(Admin admin) throws Exception {
        resetUser(admin);
    }

    @BeforeEach
    public void setup(
        User user,
        DataStore store,
        NetworkSchema schema
    )
        throws Exception
    {
        this.service = new RestNetworkService((RestUser) user);
        this.creator = service.getCreator();
        schema.meta.id = UUID.fromString(creator.network.id);
        WappstoCerts certs = new WappstoCerts(creator);
        NetworkInstance instance = new NetworkInstance(certs, schema);
        new VirtualIoTNetwork(
            schema,
            new RpcClient(
                new SSLConnection(
                    env().get(SOCKET_URL),
                    Integer.parseInt(env().get(SOCKET_PORT)),
                    certs
                )
            )
        );
        this.schema = schema;
        store.save(schema.meta.id.toString(), instance);
        service.claim(schema.meta.id.toString());
        logInBrowser(user.getId(), env().get(APP_URL));
        Thread.sleep(1000);
    }

    @Test
    public void changes_report_state() throws Exception {

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

        assertEventuallyEquals(
            "1",
            id -> service.getState((UUID) id),
            UUID.fromString(reportState)
        );
    }

    @AfterEach
    public void reset(Admin admin) throws Exception {
        resetUser(admin);
        resetRunner();
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
