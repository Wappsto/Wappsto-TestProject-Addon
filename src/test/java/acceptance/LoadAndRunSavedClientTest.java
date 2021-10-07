package acceptance;

import actions.iot.*;
import extensions.injectors.*;
import io.testproject.java.execution.results.*;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import util.*;
import wappsto.iot.*;
import wappsto.iot.network.model.*;
import wappsto.iot.ssl.model.*;
import wappsto.network.model.*;
import wappsto.rest.network.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;
import wappsto.session.*;

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