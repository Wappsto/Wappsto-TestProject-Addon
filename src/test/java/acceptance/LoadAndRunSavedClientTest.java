package acceptance;

import actions.iot.*;
import extensions.injectors.*;
import io.testproject.java.execution.results.*;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import util.Utils;
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

@ExtendWith(AdminInjector.class)
@ExtendWith(UserInjector.class)
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(DataStoreInjector.class)
public class LoadAndRunSavedClientTest {
    private static String socketUrl;
    private static String socketPort;
    private static String appUrl;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        resetUser(admin);
        socketPort = env().get(SOCKET_PORT);
        socketUrl = env().get(SOCKET_URL);
        appUrl = env().get(APP_URL);
    }

    @BeforeEach
    public void resetRunner() throws Exception {
        Utils.resetRunner();
    }

    @Test
    public void loads_network(
        User session,
        NetworkSchema schema,
        DataStore store
    )
        throws Exception
    {
        logInBrowser(session.getId(), appUrl);
        RestNetworkService service = new RestNetworkService((RestSession) session);
        CreatorResponse creator = service.getCreator();
        String networkId = creator.network.id;
        schema.meta.id = UUID.fromString(networkId);
        NetworkInstance instance = new NetworkInstance(
            new WappstoCerts(creator),
            schema
        );
        store.save(networkId, instance);
        LoadAndRunSavedClient action = new LoadAndRunSavedClient();
        action.networkId = networkId;
        action.socketUrl = socketUrl;
        action.port = socketPort;
        StepExecutionResult run = runner().run(action);
        assertEquals(
            ExecutionResult.PASSED.name().toUpperCase(),
            run.getResultType().name().toUpperCase()
        );
    }

    @Test
    public void runs_new_client_when_other_client_is_connected(
        User session,
        NetworkSchema schema,
        DataStore store
    )
        throws Exception
    {
        logInBrowser(session.getId(), appUrl);
        RestNetworkService service = new RestNetworkService((RestSession) session);
        CreatorResponse creator = service.getCreator();
        String networkId = creator.network.id;
        schema.meta.id = UUID.fromString(networkId);
        NetworkInstance instance = new NetworkInstance(
            new WappstoCerts(creator),
            schema
        );
        VirtualIoTNetwork other = new VirtualIoTNetwork(
            schema,
            new RpcClient(new SSLConnection(
                socketUrl,
                Integer.parseInt(socketPort),
                new WappstoCerts(creator)))
        );
        store.save(networkId, instance);

        LoadAndRunSavedClient action = new LoadAndRunSavedClient();
        action.networkId = networkId;
        action.socketUrl = socketUrl;
        action.port = socketPort;
        StepExecutionResult run = runner().run(action);
        assertEquals(
            ExecutionResult.PASSED.name().toUpperCase(),
            run.getResultType().name().toUpperCase()
        );
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
