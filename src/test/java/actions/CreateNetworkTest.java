package actions;

import actions.network.*;
import org.junit.jupiter.api.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.Env.*;
import static util.Utils.*;

public class CreateNetworkTest {
    private static String serviceUrl;
    private static String appUrl;
    private RestUser session;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        resetUser();
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
        session = createNewUserSession(serviceUrl);
        logInBrowser(session.getId(), appUrl);
    }

    @Test
    public void fails_to_create_when_logged_out() throws Exception {
        CreateNetwork action = createNewAction(serviceUrl);
        logBrowserOut();
        assertThrows(ExecutionException.class, () -> runner().run(action));
    }

    @Test
    public void creates_network() throws Exception {
        CreateNetwork action = createNewAction(serviceUrl);
        runner().run(action);
        assertNotNull(action.network);
    }

    @AfterEach
    public void tearDown() throws Exception {
        resetUser();
    }

    private CreateNetwork createNewAction(String serviceUrl) {
        CreateNetwork action = new CreateNetwork();
        action.serviceUrl = serviceUrl;
        return action;
    }

    private static void resetUser() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
