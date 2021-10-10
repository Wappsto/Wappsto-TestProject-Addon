package integration.rest;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.session.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(UserInjector.class)
public class UserSessionTest {

    @BeforeAll
    public static void setup(Admin admin) {
        try {
            admin.delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void creates_new_user_session(User session) {
        assertNotNull(session.getId());
    }

    @Test
    public void fetches_own_user(User session) throws Exception {
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
