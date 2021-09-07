package actions;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import util.Config;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.model.Credentials;
import wappsto.rest.session.UserSession;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.Utils.*;

public class ClaimNetworkTest {
    private static Config testConfig;

    @BeforeAll
    public static void setup() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignore) {
        }

        testConfig = new Config();
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }

    @Test
    public void claims_network() throws Exception {
        Credentials credentials = new Credentials(
            testConfig.DEVELOPER_USERNAME,
            testConfig.DEVELOPER_PASSWORD
        );

        UserSession session = new UserSession(
            credentials,
            testConfig.API_ROOT
        );

        logInBrowser(session.getId(), testConfig.APP_URL);

        ClaimNetwork action = createAction(
            testConfig.NETWORK,
            testConfig.API_ROOT
        );

        runner().run(action);
    }

    @Nested
    public class fails_to_claim_network {
        @Test
        public void when_browser_is_not_logged_in() {
            ClaimNetwork action = createAction(
                testConfig.NETWORK,
                testConfig.API_ROOT
            );

            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }

        @Test
        public void when_user_does_not_have_permissions() throws Exception {
            ClaimNetwork action = createAction(
                testConfig.NETWORK,
                testConfig.API_ROOT
            );

            UserSession session = new UserSession.Builder(
                admin(),
                testConfig.API_ROOT
            ).withCredentials(defaultUser())
                .create();

            logInBrowser(session.getId(), testConfig.APP_URL);

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

    private ClaimNetwork createAction(String network, String seriveUrl) {
        ClaimNetwork action = new ClaimNetwork();
        action.serviceUrl = seriveUrl;
        action.networkId = network;
        return action;
    }
}
