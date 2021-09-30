package rest;

import org.junit.jupiter.api.*;
import wappsto.rest.session.RestUser;

import static util.Env.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserSessionTest {
    private static String serviceUrl;

    @BeforeAll
    public static void setup() {
        serviceUrl = env().get(API_ROOT);

        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void creates_new_user_session() throws Exception {
        RestUser session = createNewUserSession(serviceUrl);
        assertNotNull(session.id);
    }

    @Test
    public void fetches_own_user() throws Exception {
        RestUser session = createNewUserSession(serviceUrl);

        assertEquals(
            defaultUser().username,
            session.fetchUser().username
        );
    }

    @AfterEach
    public void tearDown() {
        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }
}
