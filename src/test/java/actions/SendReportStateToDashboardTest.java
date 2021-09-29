package actions;

import io.testproject.java.execution.results.*;
import io.testproject.java.sdk.v2.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import org.junit.jupiter.api.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

public class SendReportStateToDashboardTest {
    private static String serviceUrl;
    private static String appUrl;
    private User session;
    private static String socketUrl;
    private static String socketPort;

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
        logInBrowser(session.id, appUrl);
    }

    @Nested
    public class fails_to_get_creator {
        @Test
        public void when_browser_is_not_logged_in() throws Exception {
            SendReportStateChangeToDashboard action = createAction(
                serviceUrl,
                socketUrl,
                socketPort
            );
            logBrowserOut();
            assertThrows(ExecutionException.class, () -> runner().run(action));
        }
    }

    @Nested
    public class fails_to_connect_iot_device {
        @Test
        public void to_invalid_url() {
            SendReportStateChangeToDashboard action = createAction(
                serviceUrl,
                "not-wappsto",
                socketPort
            );
            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void to_invalid_port() {
            SendReportStateChangeToDashboard action = createAction(
                serviceUrl,
                socketUrl,
                "8080"
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }
    }

    @Test
    public void sends_report_state_to_wappsto() throws Exception {
        SendReportStateChangeToDashboard action = createAction(
            serviceUrl,
            socketUrl,
            socketPort
        );
        StepExecutionResult run = runner().run(action);
        assertEquals(
            ExecutionResult.PASSED.name().toUpperCase(),
            run.getResultType().name().toUpperCase()
        );
    }

    private SendReportStateChangeToDashboard createAction(
        String serviceUrl,
        String socketUrl,
        String socketPort
    ) {
        SendReportStateChangeToDashboard action =
            new SendReportStateChangeToDashboard();

        action.serviceUrl = serviceUrl;
        action.socketUrl = socketUrl;
        action.port = socketPort;
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
