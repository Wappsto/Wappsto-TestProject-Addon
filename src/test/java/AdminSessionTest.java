import org.junit.jupiter.api.*;
import wappsto.rest.exceptions.*;
import wappsto.rest.model.*;
import wappsto.rest.session.AdminSession;

import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;

public class AdminSessionTest {
    private static TestConfig testConfig;
    private static Credentials credentials;
    private static AdminSession session;

    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new TestConfig();
        credentials = new Credentials(
            "test123123@sexulit.com",
            "123"
        );
        session = createNewAdminSession();
    }

    @Nested
    public class fails_to_create {
        @Test
        public void with_invalid_credentials() {
            assertThrows(Forbidden.class, () -> new AdminSession(
                new AdminCredentials(null, null),
                testConfig.API_ROOT
            ));
        }
    }

    @Test
    public void creates_a_new_session() throws Exception {
        AdminSession session = createNewAdminSession();
        assertNotNull(session.getId());
    }

    @Test
    public void registers_new_user() throws Exception {
        session.register(credentials);
        assertEquals(
            credentials.username,
            session.fetchUser(credentials.username).username);
    }

    @Test
    public void deletes_existing_user() throws Exception {
        session.register(credentials);
        session.delete(credentials.username);

        assertThrows(NotFound.class, () -> {
            session.fetchUser(credentials.username);
        });
    }

    @AfterEach
    public void tearDown() {
        try {
            session.delete(credentials.username);
        } catch (Exception ignored) {

        }
    }

    private static AdminSession createNewAdminSession() throws Exception {
        AdminSession session = new AdminSession(
            new AdminCredentials(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD
            ),
            testConfig.API_ROOT
        );
        return session;
    }
}
