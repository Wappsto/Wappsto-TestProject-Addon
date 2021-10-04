package acceptance;

import actions.network.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;
import wappsto.session.*;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(UserInjector.class)
public class CreateNetworkTest {
    private static String serviceUrl;
    private static String appUrl;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        resetUser(admin);
    }

    @BeforeEach
    public void reset(User session) throws Exception {
        resetRunner();
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
    public void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }

    private CreateNetwork createNewAction(String serviceUrl) {
        CreateNetwork action = new CreateNetwork();
        action.serviceUrl = serviceUrl;
        return action;
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
