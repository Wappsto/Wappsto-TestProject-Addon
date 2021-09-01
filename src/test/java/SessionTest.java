import org.junit.jupiter.api.*;
import wappsto.rest.exceptions.Forbidden;
import wappsto.rest.model.*;
import wappsto.rest.session.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class SessionTest {
    static TestConfig testConfig;
    static AdminSession admin;
    static Credentials credentials;
    static String serviceUrl;

    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new TestConfig();
        serviceUrl = testConfig.API_ROOT;
        admin = new AdminSession(
            new AdminCredentials(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD),
            serviceUrl
        );

        String username = "test-4242424242@seluxit.com";
        String password = "123";
        credentials = new Credentials(username, password);

        try {
            admin.delete(username);
        } catch (Exception ignored) {

        }
    }

    @Test
    public void creates_new_user() throws Exception {
        UserSession session = new UserSessionBuilder(admin, serviceUrl)
            .withCredentials(credentials)
            .create();

        assertEquals(credentials.username, session.fetchUser().username);
    }

    @AfterEach
    public void tearDown() {
        try {
            admin.delete(credentials.username);
        } catch (Exception ignored) {
        }
    }
}
