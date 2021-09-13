package rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.session.Network;
import wappsto.rest.session.core.User;
import wappsto.rest.session.model.Credentials;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Nested
    public class fails_to_claim {
        @Test
        public void invalid_network() throws Exception {
            User session = createNewUserSession(serviceUrl);
            Network network = new Network(session);
            assertThrows(
                HttpException.class,
                () -> network.claimNetwork("bad id"),
                "Bad request"
            );
        }

        @Test
        public void when_not_authorized() throws Exception {
            User session = createNewUserSession(serviceUrl);
            Network network = new Network(session);

            assertThrows(
                HttpException.class,
                () -> network.claimNetwork(env().get(NETWORK_TOKEN)),
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

        Network network = new Network(session);

        assertDoesNotThrow(
            () -> network.claimNetwork(env().get(NETWORK_TOKEN))
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
