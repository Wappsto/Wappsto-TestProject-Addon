package rest;

import org.junit.jupiter.api.*;
import util.Config;
import wappsto.rest.exceptions.*;
import wappsto.rest.model.*;
import wappsto.rest.session.AdminSession;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utils.*;

public class AdminSessionTest {
    private static Config testConfig;

    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new Config();
    }

    @Nested
    public class fails_to_create {
        @Test
        public void with_invalid_admin_credentials() {
            assertThrows(HttpException.class, () -> new AdminSession(
                new AdminCredentials(null, null),
                testConfig.API_ROOT
            ));
        }
    }

    @Test
    public void creates_a_new_session() throws Exception {
        assertNotNull(admin().getId());
    }

    @Test
    public void registers_new_user() throws Exception {
        admin().register(defaultUser());

        String expected = defaultUser().username;
        String actual =  admin().fetchUser(defaultUser().username).username;

        assertEquals(expected, actual)
    }

    @Nested
    public class fails_to_register_new_user {
        @Test
        public void with_missing_credentials () {
            Credentials credentials = new Credentials(
                null,
                null
            );

            assertThrows(
                HttpException.class,
                () -> admin().register(credentials),
                "Bad Request"
            );
        }

        @Test
        public void with_malformed_username() {
            Credentials credentials = new Credentials(
                "123",
                "123"
            );

            assertThrows(
                HttpException.class,
                () -> admin().register(credentials),
                "Bad Request"
            );
        }
    }

    @Test
    public void deletes_existing_user() throws Exception {
        admin().register(defaultUser());
        admin().delete(defaultUser().username);

        assertThrows(
            HttpException.class,
            () -> admin().fetchUser(defaultUser().username),
            "Not Found"
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
