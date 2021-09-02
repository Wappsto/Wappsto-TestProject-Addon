import actions.LogInAsAdmin;
import org.junit.jupiter.api.*;
import test.Config;

import java.io.IOException;

import static test.Utils.*;


public class LogInAdminActionTest {
    private static Config testConfig;

    @BeforeAll
    public static void setup() throws IOException {
        testConfig = new Config();
    }

    @Test
    public void logs_in_as_admin() throws Exception {
        LogInAsAdmin action = new LogInAsAdmin();
        action.adminPanel = testConfig.ADMIN_PANEL;
        action.adminUsername = testConfig.ADMIN_USERNAME;
        action.adminPassword = testConfig.ADMIN_PASSWORD;
        action.serviceUrl = testConfig.API_ROOT;

        runner().run(action);
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }
}
