package acceptance;

import actions.network.*;
import extensions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.rest.network.*;
import wappsto.network.model.*;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.Credentials;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
public class ClaimNetworkTest {
    public static final String NETWORK_FRIEND = "networkfriend@seluxit.com";
    private static String serviceUrl;
    private static String appUrl;
    private RestUser session;
    private RestUser friend;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        try {
            admin.delete(defaultUser().username);
            admin.delete(NETWORK_FRIEND);
        } catch (HttpException ignore) {
        }

    }

    @BeforeEach
    public void reset(Admin admin) throws Exception {
        resetRunner();
        session = createNewUserSession(serviceUrl, admin);
        friend = makeAFriend(admin);
    }

    @Test
    public void claims_network() throws Exception {
        RestNetworkService service = new RestNetworkService(session);
        NetworkMeta network = service.create();

        service.share(network, friend.fetchMe());

        logInBrowser(friend.getId(), appUrl);

        ClaimNetwork action = createAction(
            network.id
        );

        runner().run(action);
    }

    @Nested
    public class fails_to_claim_network {
        @Test
        public void when_browser_is_not_logged_in() {
            ClaimNetwork action = createAction(
                "token"
            );

            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }

        @Test
        public void when_network_has_not_been_shared() throws Exception {
            RestNetworkService service = new RestNetworkService(session);
            NetworkMeta network = service.create();

            ClaimNetwork action = createAction(
                network.id
            );

            logInBrowser(friend.getId(), appUrl);

            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
            admin.delete(NETWORK_FRIEND);
        } catch (HttpException ignored) {
        }
    }

    private ClaimNetwork createAction(String network) {
        ClaimNetwork action = new ClaimNetwork();
        action.serviceUrl = serviceUrl;
        action.networkId = network;
        return action;
    }

    private RestUser makeAFriend(Admin admin) throws Exception {
        return new RestUser.Builder(admin, serviceUrl)
            .withCredentials(
                new Credentials(NETWORK_FRIEND, "123")
            ).create();
    }
}
