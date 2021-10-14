package acceptance;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import util.*;
import wappsto.api.network.model.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;
import wappsto.iot.*;
import wappsto.iot.network.model.*;
import wappsto.iot.ssl.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(DataStoreInjector.class)
public class AddDeviceToRpcClientTest {

    private static String socketPort;
    private static String socketUrl;
    private static String appUrl;
    private static String serviceUrl;
    private RestUser session;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        resetUser(admin);
        socketPort = env().get(SOCKET_PORT);
        socketUrl = env().get(SOCKET_URL);
        appUrl = env().get(APP_URL);
        serviceUrl = env().get(API_ROOT);
    }

    @BeforeEach
    public void resetRunner(Admin admin) throws Exception {
        Utils.resetRunner();
        session = createNewUserSession(serviceUrl, admin);
        logInBrowser(session.getId(), appUrl);
    }

    @Test
    @Disabled
    public void adds_new_device_to_existing_client(
        NetworkSchema schema,
        DataStore store
    )
        throws Exception
    {
        RestNetworkService service = new RestNetworkService(session);
        CreatorResponse creator = service.getCreator();
        schema.meta.id = UUID.fromString(creator.network.id);
        store.save(
            creator.network.id,
            new NetworkInstance(new WappstoCerts(creator), schema)
        );
        AddDeviceToRpcClient action = new AddDeviceToRpcClient();
        action.socketUrl = socketUrl;
        action.port = socketPort;
        action.name = "Test";
        runner().run(action);
        assertNotNull(service.getDevice(UUID.fromString(action.deviceId)));
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
