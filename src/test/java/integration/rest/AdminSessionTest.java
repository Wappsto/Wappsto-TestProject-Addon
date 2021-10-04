package integration.rest;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.RestAdmin;
import wappsto.session.*;
import wappsto.session.model.AdminCredentials;
import wappsto.session.model.Credentials;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
public class AdminSessionTest {

    @Nested
    public class fails_to_create {
        @Test
        public void with_invalid_admin_credentials() {
            assertThrows(HttpException.class, () -> new RestAdmin(
                new AdminCredentials(null, null),
                env().get(API_ROOT)
            ));
        }
    }

    @Test
    public void creates_a_new_session(Admin admin) {
        assertNotNull(admin.getId());
    }

    @Test
    public void registers_new_user(Admin admin) throws Exception {
        admin.register(defaultUser());

        String expected = defaultUser().username;
        String actual =  admin.fetchUser(defaultUser().username).username;

        assertEquals(expected, actual);
    }

    @Nested
    public class fails_to_register_new_user {
        @Test
        public void with_missing_credentials (Admin admin) {
            Credentials credentials = new Credentials(
                null,
                null
            );

            assertThrows(
                HttpException.class,
                () -> admin.register(credentials),
                "Bad Request"
            );
        }

        @Test
        public void with_malformed_username(Admin admin) {
            Credentials credentials = new Credentials(
                "123",
                "123"
            );

            assertThrows(
                HttpException.class,
                () -> admin.register(credentials),
                "Bad Request"
            );
        }
    }

    @Test
    public void deletes_existing_user(Admin admin) throws Exception {
        admin.register(defaultUser());
        admin.delete(defaultUser().username);

        assertThrows(
            HttpException.class,
            () -> admin.fetchUser(defaultUser().username),
            "Not Found"
        );

    }

    @BeforeAll
    public static void setup(Admin admin) {
        deleteUser(admin);
    }

    @AfterEach
    public void tearDown(Admin admin) {
        deleteUser(admin);
    }

    private static void deleteUser(Admin admin) {
        try {
            admin.delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }
}
