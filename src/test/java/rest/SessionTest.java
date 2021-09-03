package rest;

import org.junit.jupiter.api.*;
import util.Config;
import wappsto.rest.session.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;


public class SessionTest {
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
    public void creates_new_user() throws Exception {
        UserSession session = new UserSession.Builder(admin(), serviceUrl)
            .withCredentials(defaultUser())
            .create();

        assertEquals(defaultUser().username, session.fetchUser().username);
    }

    @AfterEach
    public void tearDown() {
        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }
}
