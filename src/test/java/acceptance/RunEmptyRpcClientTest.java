package acceptance;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
public class RunEmptyRpcClientTest {

    private static String serviceUrl;
    private static String appUrl;
    private static String socketUrl;
    private static String socketPort;
    private RestUser session;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        socketUrl = env().get(SOCKET_URL);
        socketPort = env().get(SOCKET_PORT);
        resetUser(admin);
    }

    @BeforeEach
    public void reset(Admin admin) throws Exception {
        resetRunner();
        session = createNewUserSession(serviceUrl, admin);
        logInBrowser(session.getId(), appUrl);
    }

    @Test
    public void creates_empty_rpc_client() throws Exception {
        RunEmptyRpcClient action = new RunEmptyRpcClient();
        action.socketUrl = socketUrl;
        action.port = socketPort;
        action.serviceUrl = serviceUrl;
        action.name = "Test";

        runner().run(action);

        assertDoesNotThrow(() -> new RestNetworkService(session)
            .fetch(action.networkId)
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
