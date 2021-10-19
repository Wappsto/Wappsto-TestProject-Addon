package acceptance.iot;

import actions.iot.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;
import wappsto.iot.*;
import wappsto.iot.network.model.*;

import java.util.*;

import static acceptance.Util.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(DataStoreInjector.class)
public class AddNumberValueToDeviceTest {
    private static String serviceUrl;
    private static String appUrl;
    private static String socketUrl;
    private static String socketPort;
    private RestUser session;
    private NetworkSchema schema;
    private String networkId;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        socketUrl = env().get(SOCKET_URL);
        socketPort = env().get(SOCKET_PORT);
        resetUser(admin);
    }

    @BeforeEach
    public void reset(
        Admin admin,
        NetworkSchema schema,
        DataStore store
    )
        throws Exception
    {
        resetRunner();
        session = createNewUserSession(serviceUrl, admin);
        logInBrowser(session.getId(), appUrl);
        RestNetworkService service = new RestNetworkService(session);
        this.schema = createSavedNetwork(schema, store, service);
        networkId = schema.meta.id.toString();
    }

    @Test
    public void adds_value_to_a_device() throws Exception {
        AddNumberValueToDevice action = new AddNumberValueToDevice();
        action.socketUrl = socketUrl;
        action.port = socketPort;
        action.networkId = networkId;
        action.deviceId = schema.device.get(0).meta.id.toString();
        action.name = "Add Value Test";
        action.type = "Boolean";
        action.min = "0";
        action.max = "1";
        action.stepSize = "1";
        action.permissions = "rw";

        runner().run(action);
        assertEventuallyDoesNotThrow(
            value -> new RestNetworkService(session).getValue((UUID) value),
            UUID.fromString(action.valueId)
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
