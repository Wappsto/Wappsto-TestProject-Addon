package rest;

import org.junit.jupiter.api.*;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.session.core.User;
import wappsto.rest.session.model.Credentials;

import static util.Env.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserSessionTest {
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
    public void creates_new_user_session() throws Exception {
        User session = createNewUserSession(serviceUrl);

        assertNotNull(session.id);
    }

    @Test
    public void fetches_own_user() throws Exception {
        User session = createNewUserSession(serviceUrl);

        assertEquals(
            defaultUser().username,
            session.fetchUser().username
        );
    }

    @Nested
    public class fails_to_claim {
        @Test
        public void invalid_network() throws Exception {
            User session = createNewUserSession(serviceUrl);

            assertThrows(
                HttpException.class,
                () -> session.claimNetwork("bad id"),
                "Bad request"
            );
        }

        @Test
        public void when_not_authorized() throws Exception {
            User session = createNewUserSession(serviceUrl);

            assertThrows(
                HttpException.class,
                () -> session.claimNetwork(env().get(NETWORK_TOKEN)),
                "Unauthorized"
            );
        }
    }

    @Test
    public void claims_network() throws Exception {
        Credentials credentials = new Credentials(
            env().get(DEVELOPER_USERNAME),
            env().get(DEVELOPER_PASSWORD)
        );

        User session = new User(
            credentials,
            serviceUrl
        );

        assertDoesNotThrow(
            () -> session.claimNetwork(env().get(NETWORK_TOKEN))
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
