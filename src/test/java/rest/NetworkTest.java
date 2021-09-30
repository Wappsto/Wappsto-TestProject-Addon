package rest;

import org.junit.jupiter.api.*;
import wappsto.network.model.*;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.network.RestNetworkService;
import wappsto.rest.session.RestUser;
import wappsto.session.model.Credentials;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

public class NetworkTest {
    public static final String NETWORK_FRIEND = "networkfriend@seluxit.com";
    private static String serviceUrl;
    private RestUser session;
    private RestUser friend;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);

        try {
            admin().delete(defaultUser().username);
            admin().delete(NETWORK_FRIEND);
        } catch (HttpException ignored) {
        }
    }

    @BeforeEach
    public void registerUser() throws Exception {
        session = createNewUserSession(
            serviceUrl
        );
        friend = new RestUser.Builder(admin(), serviceUrl)
            .withCredentials(
                new Credentials(
                    NETWORK_FRIEND,
                    "123"
                )
            ).create();
    }

    @Test
    public void creates_new_network() throws Exception {
        RestNetworkService network = new RestNetworkService(session);

        assertNotNull(network.getCreator().network);
    }

    @Test
    public void shares_an_owned_network() throws Exception {
        RestNetworkService service = new RestNetworkService(session);
        NetworkMeta network = service.create();
        service.share(network, friend.fetchMe());

        RestNetworkService friendService = new RestNetworkService(friend);

        assertNotNull(friendService.fetch(network.id));
    }

    @Test
    public void fetches_an_owned_network() throws Exception {
        RestNetworkService networkService = new RestNetworkService(session);
        NetworkMeta network = networkService.create();
        assertEquals(network.id, networkService.fetch(network.id).id);
    }

    @Nested
    public class fails_to_claim {
        @Test
        public void invalid_network() throws Exception {
            RestNetworkService network = new RestNetworkService(session);
            assertThrows(
                HttpException.class,
                () -> network.claim("bad id"),
                "Bad request"
            );
        }

        @Test
        public void when_not_authorized() throws Exception {
            RestNetworkService service = new RestNetworkService(session);
            NetworkMeta network = service.create();
            RestNetworkService friendService = new RestNetworkService(friend);

            assertThrows(
                HttpException.class,
                () -> friendService.claim(network.id),
                "Unauthorized"
            );
        }
    }

    @Test
    public void claims_shared_network() throws Exception {
        RestNetworkService service = new RestNetworkService(session);
        NetworkMeta network = service.create();
        service.share(network, friend.fetchMe());

        RestNetworkService friendService = new RestNetworkService(friend);

        assertDoesNotThrow(() -> friendService.claim(network.id));
    }

    @AfterEach
    public void tearDown() {
        try {
            admin().delete(defaultUser().username);
            admin().delete(NETWORK_FRIEND);
        } catch (Exception ignored) {
        }
    }
}
