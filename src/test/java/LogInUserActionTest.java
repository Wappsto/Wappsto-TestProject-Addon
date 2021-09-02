
import actions.LogInWithNewUser;
import io.testproject.java.sdk.v2.Runner;
import org.junit.jupiter.api.*;
import test.Config;
import wappsto.rest.exceptions.HttpException;

import static test.Utils.*;

public class LogInUserActionTest {
    private static Config testConfig;
    private static String serviceUrl;
    private static String appUrl;

    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new Config();
        serviceUrl = testConfig.API_ROOT;
        appUrl = testConfig.APP_URL;
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException e) {
            if (!e.getMessage().equals("Not Found")) {
                throw new Exception(e.getMessage());
            }
        }
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }

    @Test
    public void logs_in_new_user() throws Exception {
        LogInWithNewUser action = new LogInWithNewUser();
        action.adminPassword = testConfig.ADMIN_PASSWORD;
        action.adminUsername = testConfig.ADMIN_USERNAME;

        action.username = defaultUser().username;
        action.password = defaultUser().password;

        action.serviceUrl = serviceUrl;
        action.appUrl = appUrl;

        runner().run(action);
    }

    @AfterEach
    public void tearDown() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
