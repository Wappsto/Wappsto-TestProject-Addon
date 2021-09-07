package rest;

import org.junit.jupiter.api.*;
import util.Config;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.model.Credentials;
import wappsto.rest.session.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserSessionTest {
    private static Config testConfig;
    private static String serviceUrl;

    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new Config();
        serviceUrl = testConfig.API_ROOT;


        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {

        }
    }

    @Test
    public void creates_new_user_session() throws Exception {
        UserSession session = createNewUserSession();

        assertNotNull(session.getId());
    }

    @Test
    public void fetches_own_user() throws Exception {
        UserSession session = createNewUserSession();

        assertEquals(
            defaultUser().username,
            session.fetchUser().username
        );
    }

    @Nested
    public class installs_wapp {
        @Test
        public void from_enum() throws Exception {
            UserSession session = createNewUserSession();

            session.install(Wapp.HISTORICAL_DATA);
            assert session.fetchWapps().size() == 1
                : "Incorrect number of wapps found";
        }

        @Test
        public void from_name() throws Exception {
            UserSession session = createNewUserSession();

            session.install("Historical Data");
            assert session.fetchWapps().size() == 1
                : "Incorrect number of wapps found";
        }
    }

    @Test
    public void fails_to_install_wapp_from_invalid_name() throws Exception {
        UserSession session = createNewUserSession();

        assertThrows(
            IllegalStateException.class,
            () -> session.install(""),
            "Wapp not found"
        );
    }

    @Nested
    public class fails_to_claim {
        @Test
        public void invalid_network() throws Exception {
            UserSession session = createNewUserSession();

            assertThrows(
                HttpException.class,
                () -> session.claimNetwork("bad id"),
                "Bad request"
            );
        }

        @Test
        public void when_not_authorized() throws Exception {
            UserSession session = createNewUserSession();

            assertThrows(
                HttpException.class,
                () -> session.claimNetwork(testConfig.NETWORK),
                "Unauthorized"
            );
        }
    }

    @Test
    public void claims_network() throws Exception {
        Credentials credentials = new Credentials(
            testConfig.DEVELOPER_USERNAME,
            testConfig.DEVELOPER_PASSWORD
        );

        UserSession session = new UserSession(
            credentials,
            serviceUrl
        );

        assertDoesNotThrow(() -> session.claimNetwork(testConfig.NETWORK));
    }

    private UserSession createNewUserSession() throws Exception {
        UserSession session = new UserSession.Builder(admin(), serviceUrl)
            .withCredentials(defaultUser())
            .create();
        return session;
    }

    @AfterEach
    public void tearDown() {
        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }
}
