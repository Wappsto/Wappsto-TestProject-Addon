package actions;

import org.junit.jupiter.api.*;
import wappsto.rest.network.*;
import wappsto.network.model.*;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.session.model.Credentials;
import wappsto.rest.session.RestUser;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.Env.*;
import static util.Utils.*;

public class ClaimNetworkTest {
    public static final String NETWORK_FRIEND = "networkfriend@seluxit.com";
    private static String serviceUrl;
    private static String appUrl;
    private RestUser session;
    private RestUser friend;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        try {
            admin().delete(defaultUser().username);
            admin().delete(NETWORK_FRIEND);
        } catch (HttpException ignore) {
        }

    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
        session = createNewUserSession(serviceUrl);
        friend = makeAFriend();
    }

    @Test
    public void claims_network() throws Exception {
        NetworkService service = new NetworkService(session);
        NetworkMeta network = service.create();

        service.share(network, friend.fetchUser());

        logInBrowser(friend.id, appUrl);

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
            NetworkService service = new NetworkService(session);
            NetworkMeta network = service.create();

            ClaimNetwork action = createAction(
                network.id
            );

            logInBrowser(friend.id, appUrl);

            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        try {
            admin().delete(defaultUser().username);
            admin().delete(NETWORK_FRIEND);
        } catch (HttpException ignored) {
        }
    }

    private ClaimNetwork createAction(String network) {
        ClaimNetwork action = new ClaimNetwork();
        action.serviceUrl = serviceUrl;
        action.networkId = network;
        return action;
    }

    private RestUser makeAFriend() throws Exception {
        return new RestUser.Builder(admin(), serviceUrl)
            .withCredentials(
                new Credentials(NETWORK_FRIEND, "123")
            ).create();
    }
}
