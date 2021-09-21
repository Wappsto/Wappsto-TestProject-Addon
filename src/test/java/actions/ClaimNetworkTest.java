package actions;

import org.junit.jupiter.api.*;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.session.model.Credentials;
import wappsto.rest.session.User;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.Env.*;
import static util.Utils.*;

public class ClaimNetworkTest {

    @BeforeAll
    public static void setup() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignore) {
        }

    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
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
            env().get(API_ROOT)
        );

        logInBrowser(session.id, env().get(APP_URL));

        ClaimNetwork action = createAction(
            env().get(NETWORK_TOKEN),
            env().get(API_ROOT)
        );

        runner().run(action);
    }

    @Nested
    public class fails_to_claim_network {
        @Test
        public void when_browser_is_not_logged_in() {
            ClaimNetwork action = createAction(
                env().get(NETWORK_TOKEN),
                env().get(API_ROOT)
            );

            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }

        @Test
        public void when_user_does_not_have_permissions() throws Exception {
            ClaimNetwork action = createAction(
                env().get(NETWORK_TOKEN),
                env().get(API_ROOT)
            );

            User session = new User.Builder(
                admin(),
                env().get(API_ROOT)
            ).withCredentials(defaultUser())
                .create();

            logInBrowser(session.id, env().get(APP_URL));

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
        } catch (HttpException ignored) {
        }
    }

    private ClaimNetwork createAction(String network, String serviceUrl) {
        ClaimNetwork action = new ClaimNetwork();
        action.serviceUrl = serviceUrl;
        action.networkId = network;
        return action;
    }
}
