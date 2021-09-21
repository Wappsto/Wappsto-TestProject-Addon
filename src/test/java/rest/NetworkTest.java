package rest;

import org.junit.jupiter.api.*;
import wappsto.rest.network.model.*;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.network.NetworkService;
import wappsto.rest.session.User;
import wappsto.rest.session.model.Credentials;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

public class NetworkTest {
    private static String serviceUrl;

    @BeforeAll
    public static void setup() {
        serviceUrl = env().get(API_ROOT);

        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void creates_new_network() throws Exception {
        User session = createNewUserSession(
            serviceUrl
        );
        NetworkService network = new NetworkService(session);

        assertNotNull(network.getCreator().network);
    }

    @Test
    public void fetches_an_owned_network() throws Exception {
        User session = createNewUserSession(
            serviceUrl
        );
        NetworkService networkService = new NetworkService(session);
        Network network = networkService.create();
        assertEquals(network.id, networkService.fetch(network.id).id);
    }

    @Nested
    public class fails_to_claim {
        @Test
        public void invalid_network() throws Exception {
            User session = createNewUserSession(serviceUrl);
            NetworkService network = new NetworkService(session);
            assertThrows(
                HttpException.class,
                () -> network.claim("bad id"),
                "Bad request"
            );
        }

        @Test
        public void when_not_authorized() throws Exception {
            User session = createNewUserSession(serviceUrl);
            NetworkService network = new NetworkService(session);

            assertThrows(
                HttpException.class,
                () -> network.claim(env().get(NETWORK_TOKEN)),
                "Unauthorized"
            );
        }
    }

    @Test
    @Disabled
    public void claims_network() throws Exception {
        Credentials credentials = new Credentials(
            env().get(DEVELOPER_USERNAME),
            env().get(DEVELOPER_PASSWORD)
        );

        User session = new User(
            credentials,
            serviceUrl
        );

        NetworkService network = new NetworkService(session);

        assertDoesNotThrow(
            () -> network.claim(env().get(NETWORK_TOKEN))
        );
    }

    @AfterEach
    public void tearDown() {
        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }
}
