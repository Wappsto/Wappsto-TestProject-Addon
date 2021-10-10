package integration.rest;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.*;
import wappsto.api.network.model.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;
import wappsto.api.session.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(NetworkServiceInjector.class)
public class NetworkTest {
    public static final String NETWORK_FRIEND = "networkfriend@seluxit.com";
    private static String serviceUrl;
    private RestUser friend;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);

        try {
            admin.delete(defaultUser().username);
            admin.delete(NETWORK_FRIEND);
        } catch (HttpException ignored) {
        }
    }

    @BeforeEach
    public void registerUser(Admin admin) throws Exception {
        friend = new RestUser.Builder(admin, serviceUrl)
            .withCredentials(
                new Credentials(
                    NETWORK_FRIEND,
                    "123"
                )
            ).create();
    }

    @Test
    public void creates_new_network(NetworkService service) throws Exception {
        assertNotNull(service.getCreator().network);
    }

    @Test
    public void shares_an_owned_network(NetworkService service) throws Exception {
        NetworkMeta network = service.create();
        service.share(network, friend.fetchMe());

        RestNetworkService friendService = new RestNetworkService(friend);

        assertNotNull(friendService.fetch(network.id));
    }

    @Test
    public void fetches_an_owned_network(NetworkService service) throws Exception {
        NetworkMeta network = service.create();
        assertEquals(network.id, service.fetch(network.id).id);
    }

    @Nested
    public class fails_to_claim {
        @Test
        public void invalid_network(NetworkService service) {
            assertThrows(
                HttpException.class,
                () -> service.claim("bad id"),
                "Bad request"
            );
        }

        @Test
        public void when_not_authorized(NetworkService service) throws Exception {
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
    public void claims_shared_network(NetworkService service) throws Exception {
        NetworkMeta network = service.create();
        service.share(network, friend.fetchMe());

        RestNetworkService friendService = new RestNetworkService(friend);

        assertDoesNotThrow(() -> friendService.claim(network.id));
    }

    @AfterEach
    public void tearDown(Admin admin) {
        try {
            admin.delete(defaultUser().username);
            admin.delete(NETWORK_FRIEND);
        } catch (Exception ignored) {
        }
    }
}
