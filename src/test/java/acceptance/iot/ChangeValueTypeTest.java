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
import wappsto.iot.network.model.*;

import java.util.*;

import static acceptance.Util.*;
import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(NetworkSchemaInjector.class)
@ExtendWith(DataStoreInjector.class)
public class ChangeValueTypeTest {
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
    @Disabled
    public void changes_existing_value_to_different_type() throws Exception {
        ChangeValueType action = new ChangeValueType();
        action.socketUrl = socketUrl;
        action.port = socketPort;
        action.networkId = networkId;
        action.valueId = schema.device.get(0).value.get(0).meta.id.toString();
        action.name = "Change value test";
        action.type = "Range";
        action.min = "50";
        action.max = "100";
        action.stepSize = "5";
        action.permissions = "r";

        runner().run(action);
        Thread.sleep(5000);
        ValueResponse value = new RestNetworkService(session)
            .getValue(UUID.fromString(action.valueId));
        assertEquals("Change value test", value.name);
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
