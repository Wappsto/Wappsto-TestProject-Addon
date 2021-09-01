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
    public static void setup() throws IOException, Forbidden {
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
                new AdminCredentials(null, null)
            ));
        }
    }

    @Test
    public void creates_a_new_session() throws Forbidden {
        AdminSession session = createNewAdminSession();
        assertNotNull(session.getId());
    }

    @Test
    public void registers_new_user() throws Forbidden {
        session.register(credentials);
        assertEquals(
            credentials.username,
            session.fetchUser(credentials.username).username);
    }

    @Test
    public void deletes_existing_user() throws Forbidden {
        session.register(credentials);
        session.delete(credentials.username);

        assertThrows(NotFound.class, () -> {
            session.fetchUser(credentials.username);
        });
    }

    @AfterEach
    public void tearDown() {
        session.delete(credentials.username);
    }

    private static AdminSession createNewAdminSession() throws Forbidden {
        AdminSession session = new AdminSession(
            new AdminCredentials(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD
            )
        );
        return session;
    }
}
