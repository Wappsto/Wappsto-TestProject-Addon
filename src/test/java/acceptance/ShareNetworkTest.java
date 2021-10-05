package acceptance;

import actions.network.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.network.*;
import wappsto.rest.network.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;
import wappsto.session.*;
import wappsto.session.model.*;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
public class ShareNetworkTest {
    private static String serviceUrl;
    private static String appUrl;
    private RestUser user;
    private RestUser friend;
    private static String friendUsername;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        friendUsername = "friend123@seluxit.com";
        deleteUsers(admin);
    }

    @BeforeEach
    public void reset(Admin admin) throws Exception {
        resetRunner();

        user = createNewUserSession(serviceUrl, admin);
        friend = new RestUser.Builder(admin, serviceUrl)
            .withCredentials(new Credentials(friendUsername, "123"))
            .create();
        logInBrowser(user.getId(), appUrl);
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
            NetworkService service = new RestNetworkService(user);
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
        RestNetworkService selfService = new RestNetworkService(user);
        RestNetworkService friendService = new RestNetworkService(friend);

        String network = selfService.create().id;
        ShareNetwork action = createNewAction(
            serviceUrl,
            network,
            friendUsername
        );

        runner().run(action);
        assertDoesNotThrow(() -> friendService.claim(network));
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        deleteUsers(admin);
    }

    private static void deleteUsers(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
        try {
            admin.delete(friendUsername);
        } catch (HttpException ignored) {
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

}
