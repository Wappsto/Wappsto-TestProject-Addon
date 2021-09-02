package actions;

import org.junit.jupiter.api.*;
import util.Config;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


public class LogInAdminTest {
    private static Config testConfig;

    @BeforeAll
    public static void setup() throws IOException {
        testConfig = new Config();
    }

    @Test
    public void logs_in_as_admin() throws Exception {
        LogInAsAdmin action = createAction(
            testConfig.ADMIN_PANEL,
            testConfig.ADMIN_USERNAME,
            testConfig.ADMIN_PASSWORD,
            testConfig.API_ROOT);

        runner().run(action);
    }

    @Nested
    public class fails_to_log_in {
        @Test
        public void with_invalid_credentials() {
            LogInAsAdmin action = createAction(
                testConfig.ADMIN_PANEL,
                "123",
                "123",
                testConfig.API_ROOT
            );

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }

        @Test
        public void with_invalid_service_url() {
            LogInAsAdmin action = createAction(
                testConfig.ADMIN_PANEL,
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD,
                "123"
            );

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }
    }

    private LogInAsAdmin createAction(
        String adminPanel,
        String adminUsername,
        String adminPassword,
        String serviceUrl
    ) {
        LogInAsAdmin action = new LogInAsAdmin();
        action.adminPanel = adminPanel;
        action.adminUsername = adminUsername;
        action.adminPassword = adminPassword;
        action.serviceUrl = serviceUrl;
        return action;
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }
}
