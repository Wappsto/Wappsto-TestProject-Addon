package acceptance.rest;

import actions.session.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.session.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
public class CreateNewUserTest {
    private static String adminUsername;
    private static String adminPassword;
    private static String serviceUrl;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        adminUsername = env().get(ADMIN_USERNAME);
        adminPassword = env().get(ADMIN_PASSWORD);
        serviceUrl = env().get(API_ROOT);
        resetUser(admin);
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }

    @Test
    public void registers_new_user(Admin admin) throws Exception {
        CreateNewUser action = new CreateNewUser();
        action.adminUsername = adminUsername;
        action.adminPassword = adminPassword;
        action.serviceUrl = serviceUrl;
        action.username = defaultUser().username;
        action.password = defaultUser().password;

        runner().run(action);
        assertDoesNotThrow(() -> admin.fetchUser(defaultUser().username));
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        resetUser(admin);
    }

    private static void resetUser(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
