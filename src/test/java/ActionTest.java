import actions.DeleteUser;
import actions.LogInWithNewUser;
import io.testproject.java.enums.AutomatedBrowserType;
import io.testproject.java.sdk.v2.Runner;
import org.junit.jupiter.api.*;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.model.Credentials;
import wappsto.rest.session.AdminSession;

public class ActionTest {
    private static Runner runner;
    private static TestConfig testConfig;
    private String username;
    private String password;
    private static AdminSession admin;
    private static String serviceUrl;
    private static String appUrl;

    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new TestConfig();
        serviceUrl = testConfig.API_ROOT;
        appUrl = testConfig.APP_URL;
        admin = new AdminSession(
            new AdminCredentials(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD
            ),
            serviceUrl
        );

        runner = Runner.createWeb(
            testConfig.DEV_TOKEN,
            AutomatedBrowserType.Chrome);
    }

    @Test
    public void logs_in_new_user() throws Exception {
        LogInWithNewUser action = new LogInWithNewUser();
        action.adminPassword = testConfig.ADMIN_PASSWORD;
        action.adminUsername = testConfig.ADMIN_USERNAME;
        username = "test123123@seluxit.com";
        password = "123";

        action.username = username;
        action.password = password;

        action.serviceUrl = serviceUrl;
        action.appUrl = appUrl;
        runner.run(action);
    }

    @Test
    public void deletes_user() throws Exception {
        DeleteUser action = new DeleteUser();

        username = "test123@seluxit.com";
        admin.register(new Credentials(
            username,
            "123"
        ));

        action.serviceUrl = serviceUrl;
        action.username = username;
        action.adminPassword = testConfig.ADMIN_PASSWORD;
        action.adminUsername = testConfig.ADMIN_USERNAME;

        runner.run(action);
    }

    @AfterEach
    public void tearDown() {
        try {
            admin.delete(username);
        } catch (Exception ignored) {

        }
    }
}
