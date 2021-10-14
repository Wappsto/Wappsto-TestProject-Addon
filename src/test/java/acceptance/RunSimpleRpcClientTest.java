package acceptance;

import actions.iot.*;
import extensions.injectors.*;
import io.testproject.java.execution.results.*;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.model.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.rest.session.*;
import wappsto.api.session.*;
import wappsto.iot.filesystem.*;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;


@ExtendWith(AdminInjector.class)
public class RunSimpleRpcClientTest {
    private static String serviceUrl;
    private static String appUrl;
    private RestUser session;
    private static String socketUrl;
    private static String socketPort;
    private static final String min = "0";
    private static final String max = "1";
    private static final String stepSize = "1";
    private static final String type = "Boolean";

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        socketUrl = env().get(SOCKET_URL);
        socketPort = env().get(SOCKET_PORT);
        resetUser(admin);
    }

    @BeforeEach
    public void reset(Admin admin) throws Exception {
        resetRunner();
        session = createNewUserSession(serviceUrl, admin);
        logInBrowser(session.getId(), appUrl);
    }

    @Nested
    public class fails_to_get_creator {
        @Test
        public void when_browser_is_not_logged_in() throws Exception {
            RunSimpleRPCClient action = createAction(
                serviceUrl,
                socketUrl,
                socketPort,
                min, max, stepSize, type);
            logBrowserOut();
            assertThrows(ExecutionException.class, () -> runner().run(action));
        }
    }

    @Nested
    public class fails_to_connect_iot_device {
        @Test
        public void to_invalid_url() {
            RunSimpleRPCClient action = createAction(
                serviceUrl,
                "not-wappsto",
                socketPort,
                min,
                max,
                stepSize,
                type
            );
            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void to_invalid_port() {
            RunSimpleRPCClient action = createAction(
                serviceUrl,
                socketUrl,
                "11006",
                min,
                max,
                stepSize,
                type
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }
    }

    @Test
    public void sends_report_state_to_wappsto() throws Exception {
        RunSimpleRPCClient action = createAction(
            serviceUrl,
            socketUrl,
            socketPort,
            min,
            max,
            stepSize,
            type
        );
        StepExecutionResult run = runner().run(action);
        assertEquals(
            ExecutionResult.PASSED.name().toUpperCase(),
            run.getResultType().name().toUpperCase()
        );
    }

    @Test
    public void saves_itself_on_the_file_system() throws Exception {
        RunSimpleRPCClient action = createAction(
            serviceUrl,
            socketUrl,
            socketPort,
            min,
            max,
            stepSize,
            type
        );
        runner().run(action);

        String uuidFromFile = ((NetworkInstance) new FileSystemJsonDataStore(
            "./saved_instance/"
        ).load(action.networkId, NetworkInstance.class))
            .schema.meta.id.toString();
        assertEquals(
            action.networkId,
            uuidFromFile
        );

    }

    @Test
    @Tag("regression")
    public void updates_control_state_more_than_once() throws Exception {
        RestNetworkService service = new RestNetworkService(session);
        RunSimpleRPCClient action = createAction(
            serviceUrl,
            socketUrl,
            socketPort,
            min,
            max,
            stepSize,
            type
        );
        runner().run(action);
        Thread.sleep(3000);
        service.claim(action.networkId);
        service.updateState(UUID.fromString(action.controlState), "1");
        Thread.sleep(2000);
        service.updateState(UUID.fromString(action.controlState), "something");
        Thread.sleep(2000);
        assertEquals(
            "something",
            service.getState(UUID.fromString(action.controlState))
        );
    }

    private RunSimpleRPCClient createAction(
        String serviceUrl,
        String socketUrl,
        String socketPort,
        String min,
        String max,
        String stepSize,
        String type
    ) {
        RunSimpleRPCClient action = new RunSimpleRPCClient();
        action.serviceUrl = serviceUrl;
        action.socketUrl = socketUrl;
        action.port = socketPort;
        action.min = min;
        action.max = max;
        action.stepSize = stepSize;
        action.type = type;
        action.name = "Test";
        return action;
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
