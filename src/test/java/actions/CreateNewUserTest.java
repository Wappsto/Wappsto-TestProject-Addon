package actions;

import org.junit.jupiter.api.*;
import wappsto.rest.request.exceptions.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static util.Env.*;
import static util.Utils.*;

public class CreateNewUserTest {
    private static String adminUsername;
    private static String adminPassword;
    private static String serviceUrl;

    @BeforeAll
    public static void setup() throws Exception {
        adminUsername = env().get(ADMIN_USERNAME);
        adminPassword = env().get(ADMIN_PASSWORD);
        serviceUrl = env().get(API_ROOT);
        resetUser();
    }

    @Test
    public void registers_new_user() throws Exception {
        CreateNewUser action = new CreateNewUser();
        action.adminUsername = adminUsername;
        action.adminPassword = adminPassword;
        action.serviceUrl = serviceUrl;
        action.username = defaultUser().username;
        action.password = defaultUser().password;

        runner().run(action);
        assertDoesNotThrow(() -> admin().fetchUser(defaultUser().username));
    }

    @AfterEach
    public void tearDown() throws Exception {
        resetUser();
    }

    private static void resetUser() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
