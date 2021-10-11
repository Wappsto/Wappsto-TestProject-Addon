package acceptance;

import actions.network.*;
import extensions.injectors.*;
import io.testproject.java.execution.results.*;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;

import static actions.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(UserInjector.class)
public class CreateDeviceUnderNetworkTest {
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
    @Disabled
    public void creates_device_under_network() throws Exception {
        CreateDeviceUnderNetwork action = new CreateDeviceUnderNetwork();
        action.serviceUrl = serviceUrl;
        action.networkId = new RestNetworkService(
            new RestUser(getSessionFrom(runner().getDriver()), serviceUrl))
            .createNetwork().id;
        StepExecutionResult run = runner().run(action);
        assertEquals(
            ExecutionResult.PASSED.name().toUpperCase(),
            run.getResultType().name().toUpperCase()
        );
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }


}
