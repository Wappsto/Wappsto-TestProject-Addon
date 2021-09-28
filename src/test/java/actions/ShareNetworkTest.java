package actions;

import io.testproject.java.sdk.v2.drivers.*;
import org.junit.jupiter.api.*;
import wappsto.rest.network.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;
import wappsto.rest.session.model.*;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

public class ShareNetworkTest {
    private static String serviceUrl;
    private static String appUrl;
    private User user;
    private User friend;
    private static String friendUsername;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        friendUsername = "friend123@seluxit.com";
        try {
            admin().delete(defaultUser().username);
            admin().delete(friendUsername);
        } catch (HttpException ignored) {
        }
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();

        user = createNewUserSession(serviceUrl);
        friend = new User.Builder(admin(), serviceUrl)
            .withCredentials(new Credentials(friendUsername, "123"))
            .create();
        logInBrowser(user.id, appUrl);
    }

    @Nested
    public class fails_to_share {
        @Test
        public void invalid_network() {
            ShareNetwork action = createNewAction(serviceUrl, "", friendUsername);
            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }

        @Test
        public void when_browser_is_not_logged_in() throws Exception {
            NetworkService service = new NetworkService(user);
            String network = service.create().id;
            ShareNetwork action = createNewAction(
                serviceUrl,
                network,
                friendUsername
            );
            logBrowserOut();

            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }
    }

    @Test
    public void shares_network_with_other_user() throws Exception {
        NetworkService selfService = new NetworkService(user);
        NetworkService friendService = new NetworkService(friend);

        String network = selfService.create().id;
        ShareNetwork action = createNewAction(
            serviceUrl,
            network,
            friendUsername
        );

        runner().run(action);
        assertDoesNotThrow(() -> friendService.claim(network));
    }


    private ShareNetwork createNewAction(
        String serviceUrl,
        String network,
        String friendUsername
    ) {
        ShareNetwork action = new ShareNetwork();
        action.serviceUrl = serviceUrl;
        action.network = network;
        action.other = friendUsername;
        return action;
    }

    @AfterEach
    public void tearDown() throws Exception {
        try {
            admin().delete(defaultUser().username);
            admin().delete(friendUsername);
        } catch (HttpException ignored) {
        }
    }
}
