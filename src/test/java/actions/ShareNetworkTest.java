package actions;

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
    private User user;
    private User friend;
    private NetworkService service;
    private static String friendUsername;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        friendUsername = "friend123@seluxit.com";
        try {
            admin().delete(defaultUser().username);
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
        service = new NetworkService(user);
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
        public void invalid_user() throws Exception {
            NetworkService service = new NetworkService(user);
            String network = service.create().id;
            ShareNetwork action = createNewAction(serviceUrl, network, "");
            assertThrows(
                ExecutionException.class,
                () -> runner().run(action)
            );
        }

        @Test
        public void when_browser_is_not_logged_in() {

        }
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
