package actions;

import actions.iot.*;
import io.testproject.java.execution.results.*;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import org.junit.jupiter.api.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

public class RunSimpleRPCClientTest {
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
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        appUrl = env().get(APP_URL);
        socketUrl = env().get(SOCKET_URL);
        socketPort = env().get(SOCKET_PORT);
        resetUser();
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
        session = createNewUserSession(serviceUrl);
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

    private RunSimpleRPCClient createAction(
        String serviceUrl,
        String socketUrl,
        String socketPort,
        String min,
        String max,
        String stepSize,
        String type
    ) {
        RunSimpleRPCClient action =
            new RunSimpleRPCClient();

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
    public void tearDown() throws Exception {
        resetUser();
    }

    private static void resetUser() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

}
