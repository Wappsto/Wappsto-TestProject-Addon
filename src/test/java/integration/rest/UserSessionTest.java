package integration.rest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import extensions.*;
import wappsto.rest.session.*;
import wappsto.session.*;

import static util.Env.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AdminInjector.class)
public class UserSessionTest {
    private static String serviceUrl;

    @BeforeAll
    public static void setup(Admin admin) {
        serviceUrl = env().get(API_ROOT);

        try {
            admin.delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void creates_new_user_session(Admin admin) throws Exception {
        RestUser session = createNewUserSession(serviceUrl, admin);
        assertNotNull(session.getId());
    }

    @Test
    public void fetches_own_user(Admin admin) throws Exception {
        RestUser session = createNewUserSession(serviceUrl, admin);

        assertEquals(
            defaultUser().username,
            session.fetchMe().username
        );
    }

    @AfterEach
    public void tearDown(Admin admin) {
        try {
            admin.delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }
}
