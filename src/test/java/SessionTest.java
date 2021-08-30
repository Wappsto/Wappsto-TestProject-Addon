import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wappsto.rest.*;
import wappsto.rest.model.Credentials;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class SessionTest {
    static TestConfig testConfig;
    static AdminSession admin;

    Credentials credentials;

    @BeforeAll
    public static void setup() throws IOException {
        testConfig = new TestConfig();
        admin = new AdminSession(
            new Credentials(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD,
                true)
        );
    }

    @Test
    public void creates_new_user() throws Exception {
        String username = "test-4242424242@seluxit.com";
        String password = "123";
        credentials = new Credentials(username, password);
        UserSession session = new SessionBuilder(admin)
            .withCredentials(new Credentials(username, password))
            .create();

        assertEquals(username, session.fetchUser().username);
    }

    @AfterEach
    public void tearDown() {
        admin.delete(credentials.username);
    }
}
