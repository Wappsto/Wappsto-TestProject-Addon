import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wappsto.rest.*;
import wappsto.rest.model.AdminCredentials;
import wappsto.rest.model.Credentials;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class SessionTest {
    static TestConfig testConfig;
    static AdminSession admin;

    static Credentials credentials;

    @BeforeAll
    public static void setup() throws IOException {
        testConfig = new TestConfig();
        admin = new AdminSession(
            new AdminCredentials(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD)
        );

        String username = "test-4242424242@seluxit.com";
        String password = "123";
        credentials = new Credentials(username, password);

        if (admin.fetchUser(username) != null) {
            admin.delete(username);
        }
    }

    @Test
    public void creates_new_user() throws Exception {
        UserSession session = new SessionBuilder(admin)
            .withCredentials(credentials)
            .create();

        assertEquals(credentials.username, session.fetchUser().username);
    }

    @AfterEach
    public void tearDown() {
        admin.delete(credentials.username);
    }
}
