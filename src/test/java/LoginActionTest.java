import actions.LoggedInUser;
import io.testproject.java.enums.AutomatedBrowserType;
import io.testproject.java.sdk.v2.Runner;
import org.junit.jupiter.api.*;
import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.session.AdminSession;

import java.io.IOException;

public class LoginActionTest {
    private static Runner runner;
    private static TestConfig testConfig;
    private String username;
    private String password;
    private static AdminSession admin;

    @BeforeAll
    public static void setup() throws InstantiationException, IOException, Forbidden {
        testConfig = new TestConfig();
        admin = new AdminSession(
            new AdminCredentials(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD
            )
        );

        runner = Runner.createWeb(
            testConfig.DEV_TOKEN,
            AutomatedBrowserType.Chrome);
    }

    @Test
    public void logs_in_new_user() throws Exception {
        LoggedInUser action = new LoggedInUser();
        action.adminPassword = testConfig.ADMIN_PASSWORD;
        action.adminUsername = testConfig.ADMIN_USERNAME;
        username = "test123123@seluxit.com";
        password = "123";

        action.username = username;
        action.password = password;
        runner.run(action);
    }

    @AfterEach
    public void tearDown() {
        admin.delete(username);
    }
}
