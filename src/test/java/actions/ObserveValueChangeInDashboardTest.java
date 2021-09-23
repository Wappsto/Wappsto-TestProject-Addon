package actions;

import org.junit.jupiter.api.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;
import wappsto.rest.network.*;
import wappsto.rest.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import static iot.Utils.defaultNetwork;
import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.*;

public class ObserveValueChangeInDashboardTest {
    private static String serviceUrl;
    private User session;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
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

    }

    @AfterEach
    public void tearDown() throws Exception {
        setup();
    }
}
